package ru.tver.tstu.backend.syntax;

import org.objectweb.asm.tree.*;
import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.model.enums.*;

import java.util.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Класс расширяющий {@link RecursiveDescentParser}, добавляющий методы для генерации кода
 */
public class SyntaxParserWithBytecodeGen extends RecursiveDescentParser {

    private static final String RUN = "run";

    private final List<Keyword> currentExpression;

    private final Stack<LabelNode> gotoLabels;
    private final Stack<LabelNode> whileLabels;

    private final ByteCodeBuilder byteCodeBuilder;

    //default method node (main method analog)
    private MethodNode currentMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);

    public SyntaxParserWithBytecodeGen(List<Keyword> lexems, IdentifierTable nameTable, ByteCodeBuilder byteCodeBuilder) {
        super(lexems, nameTable);
        iterator = lexems.iterator();
        currentExpression = new ArrayList<>();
        gotoLabels = new Stack<>();
        whileLabels = new Stack<>();
        this.byteCodeBuilder = byteCodeBuilder;
    }

    @Override
    protected void block() {
        if (isAccept(Command.CONST)) {
            do {
                Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
                if (currentKeyword.lex == Lexem.NAME) {
                    updateIdentifierInfo(IdentifierCategory.CONST);
                }
                isExpect(Lexem.NAME, 4);
                isExpect(Lexem.EQUAL, 3);
                if (currentKeyword.lex == Lexem.NUMBER) {
                    identifier.setValue(Integer.parseInt(currentKeyword.word));
                }
                isExpect(Lexem.NUMBER, 2);
            } while (isAccept(Lexem.SEMI));
            isExpect(Lexem.SEMICOLON, 5);
        }
        if (isAccept(Command.VAR)) {
            do {
                if (currentKeyword.lex == Lexem.NAME) {
                    // if method is default, variable becomes class field
                    if (currentMethodNode.name.equals(RUN)) {
                        updateIdentifierInfo(IdentifierCategory.CLASS_VAR);
                    } else {
                        updateIdentifierInfo(IdentifierCategory.LOCAL_VAR);
                    }
                }
                // if method is default, variable becomes class field
                if (currentMethodNode.name.equals(RUN)) {
                    byteCodeBuilder.addField(new FieldNode(ACC_PRIVATE + ACC_STATIC, currentKeyword.word, "I", null, TOP));
                }
                isExpect(Lexem.NAME, 4);
            } while (isAccept(Lexem.SEMI));
            isExpect(Lexem.SEMICOLON, 5);
        }
        while (isAccept(Command.PROCEDURE)) {
            if (currentKeyword.lex == Lexem.NAME) {
                updateIdentifierInfo(IdentifierCategory.PROCEDURE_NAME);
            }
            currentMethodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, currentKeyword.word, "()V", null, null);
            isExpect(Lexem.NAME, 4);
            isExpect(Lexem.SEMICOLON, 5);
            block();
            isExpect(Lexem.SEMICOLON, 5);
            // get back to default method
            byteCodeBuilder.addInstruction(currentMethodNode, new InsnNode(RETURN));
            currentMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        }
        statement();
    }

    @Override
    protected void factor() {
        if (currentKeyword.lex == Lexem.NAME) { // var or const
            currentExpression.add(currentKeyword);
            isAccept(Lexem.NAME);
        } else if (currentKeyword.lex == Lexem.NUMBER) {
            currentExpression.add(currentKeyword);
            isAccept(Lexem.NUMBER);
        } else if (isAccept(Lexem.LEFT_BRACKET)) {
            expression();
            currentExpression.add(currentKeyword);
            isExpect(Lexem.RIGHT_BRACKET, 22);
        } else {
            error(12);
            getNextKeyword();
        }
    }

    @Override
    protected void term() {
        factor();
        while (currentKeyword.lex == Lexem.MULTIPLICATION || currentKeyword.lex == Lexem.DIVISION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
            factor();
        }
    }

    @Override
    protected void expression() {
        if (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
        }
        term();
        while (currentKeyword.lex == Lexem.ADDITION || currentKeyword.lex == Lexem.SUBTRACTION) {
            currentExpression.add(currentKeyword);
            getNextKeyword();
            term();
        }
    }

    @Override
    protected void condition() {
        LabelNode ifLabel = new LabelNode();
        gotoLabels.push(new LabelNode());

        if (isAccept(Command.ODD)) {
            evaluateExpression();
            byteCodeBuilder.addInstruction(currentMethodNode, new InsnNode(ICONST_2));
            byteCodeBuilder.addInstruction(currentMethodNode, new InsnNode(IREM));
            byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IFEQ, ifLabel));
            byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
            byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
        } else {
            evaluateExpression();
            Lexem operator = currentKeyword.lex;
            switch (operator) {
                case EQUAL:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPEQ, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                case NOT_EQUAL:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPNE, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                case LESS_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPLT, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                case LESS_OR_EQUAL_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPLE, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                case MORE_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPGT, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                case MORE_OR_EQUAL_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(IF_ICMPGE, ifLabel));
                    byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, gotoLabels.peek()));
                    byteCodeBuilder.addInstruction(currentMethodNode, ifLabel);
                    break;
                default:
                    error(20);
                    getNextKeyword();
                    break;
            }
        }
    }

    private void evaluateExpression() {
        currentExpression.clear();
        expression();
        ExpressionParser expressionParser = new ExpressionParser(currentExpression, identifierTable, currentMethodNode, byteCodeBuilder);
        if (expressionParser.parseExpression()) {
            hasErrors = true;
        }
    }

    @Override
    protected void statement() {
        Identifier identifier;
        if (currentKeyword.lex == Lexem.NAME) {
            identifier = identifierTable.getIdentifier(currentKeyword.word);
        } else {
            error(11);
            getNextKeyword();
            return;
        }
        if (isAccept(IdentifierCategory.LOCAL_VAR)) {
            isExpect(Lexem.ASSIGN, 19);
            evaluateExpression();
            byteCodeBuilder.addInstruction(currentMethodNode, new VarInsnNode(ISTORE, Integer.parseInt(identifier.getAddress())));
            addPrintBytecodeCommands(identifier);
        } else if (isAccept(IdentifierCategory.CLASS_VAR)) {
            isExpect(Lexem.ASSIGN, 19);
            evaluateExpression();
            byteCodeBuilder.addInstruction(currentMethodNode, new FieldInsnNode(PUTSTATIC, "ClassTest",
                    identifier.getName(), "I"));
            addPrintBytecodeCommands(identifier);
        } else if (isAccept(Command.CALL)) {
            if (identifierTable.getIdentifier(currentKeyword.word).getCategory().equals(IdentifierCategory.PROCEDURE_NAME)) {
                byteCodeBuilder.addInstruction(currentMethodNode, new MethodInsnNode(INVOKESTATIC, "ClassTest",
                        currentKeyword.word, "()V"));
                isExpect(Lexem.NAME, 14);
            } else {
                error(11);
                getNextKeyword();
            }
        } else if (isAccept(Command.BEGIN)) {
            do {
                statement();
            } while (isAccept(Lexem.SEMICOLON));
            isExpect(Command.END, 17);
        } else if (isAccept(Command.IF)) {
            condition();
            isExpect(Command.THEN, 16);
            statement();
            byteCodeBuilder.addInstruction(currentMethodNode, gotoLabels.pop());
        } else if (isAccept(Command.WHILE)) {
            whileLabels.push(new LabelNode());
            byteCodeBuilder.addInstruction(currentMethodNode, whileLabels.peek());
            condition();
            isExpect(Command.DO, 18);
            statement();
            byteCodeBuilder.addInstruction(currentMethodNode, new JumpInsnNode(GOTO, whileLabels.pop()));
            byteCodeBuilder.addInstruction(currentMethodNode, gotoLabels.pop());
        } else {
            error(11);
            getNextKeyword();
        }
    }

    private void addPrintBytecodeCommands(Identifier identifier) {
        byteCodeBuilder.addInstruction(currentMethodNode, new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        if (identifier.getCategory() == IdentifierCategory.LOCAL_VAR) {
            byteCodeBuilder.addInstruction(currentMethodNode, new VarInsnNode(ILOAD, Integer.parseInt(identifier.getAddress())));
        }
        if (identifier.getCategory() == IdentifierCategory.CLASS_VAR) {
            byteCodeBuilder.addInstruction(currentMethodNode, new FieldInsnNode(GETSTATIC, "ClassTest",
                    identifier.getName(), "I"));
        }

        byteCodeBuilder.addInstruction(currentMethodNode, new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false));
    }


    private void updateIdentifierInfo(IdentifierCategory category) {
        identifierTable.getIdentifier(currentKeyword.word).setCategory(category);
    }

    @Override
    public boolean checkSyntax() {
        program();
        byteCodeBuilder.addInstruction(currentMethodNode, new InsnNode(RETURN));
        return !hasErrors;
    }
}
