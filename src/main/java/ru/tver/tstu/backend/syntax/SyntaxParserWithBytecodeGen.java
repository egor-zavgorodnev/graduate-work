package ru.tver.tstu.backend.syntax;

import org.objectweb.asm.tree.*;
import ru.tver.tstu.backend.INameTable;
import ru.tver.tstu.backend.generator.bytecode.BCG;
import ru.tver.tstu.backend.model.Identifier;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.backend.model.enums.Command;
import ru.tver.tstu.backend.model.enums.IdentifierCategory;
import ru.tver.tstu.backend.model.enums.Lexem;
import ru.tver.tstu.backend.structures.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class SyntaxParserWithBytecodeGen extends RecursiveDescentParser {

    private static final String RUN = "run";

    private List<Keyword> currentExpression;
    private LabelNode ifLabel;
    private LabelNode gotoLabel;
    private LabelNode whileLabel;

    private int currentDataAddress = 3; // because exists system vars RA,DL,SL
    private int currentLevel = 0;

    //default method node (main method analog)
    MethodNode currentMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);

    public SyntaxParserWithBytecodeGen(List<Keyword> lexems, INameTable nameTable) {
        super(lexems, nameTable);
        iterator = lexems.iterator();
        currentExpression = new ArrayList<>();
    }

    @Override
    protected void block() {
        currentLevel++;
        if (isAccept(Command.CONST)) {
            do {
                Identifier identifier = identifierTable.getIdentifier(currentKeyword.word);
                if (currentKeyword.lex == Lexem.NAME) {
                    updateIdentifierInfo(IdentifierCategory.CONST, currentLevel, "0");
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
                        updateIdentifierInfo(IdentifierCategory.CLASS_VAR, currentLevel, String.valueOf(currentDataAddress++));
                    } else {
                        updateIdentifierInfo(IdentifierCategory.LOCAL_VAR, currentLevel, String.valueOf(currentDataAddress++));
                    }
                }
                // if method is default, variable becomes class field
                if (currentMethodNode.name.equals(RUN)) {
                    BCG.addField(new FieldNode(ACC_PRIVATE + ACC_STATIC, currentKeyword.word, "I", null, TOP));
                }
                isExpect(Lexem.NAME, 4);
            } while (isAccept(Lexem.SEMI));
            isExpect(Lexem.SEMICOLON, 5);
        }
        while (isAccept(Command.PROCEDURE)) {
            if (currentKeyword.lex == Lexem.NAME) {
                updateIdentifierInfo(IdentifierCategory.PROCEDURE_NAME, currentLevel, "0");
            }
            currentMethodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, currentKeyword.word, "()V", null, null);
            isExpect(Lexem.NAME, 4);
            isExpect(Lexem.SEMICOLON, 5);
            block();
            isExpect(Lexem.SEMICOLON, 5);
            // get back to default method
            BCG.addInstr(currentMethodNode, new InsnNode(RETURN));
            currentMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        }
        statement();
        currentLevel--;
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
        ifLabel = new LabelNode();
        gotoLabel = new LabelNode();
        whileLabel = new LabelNode();

        if (isAccept(Command.ODD)) {
            BCG.addInstr(currentMethodNode, whileLabel);
            evaluateExpression();
            BCG.addInstr(currentMethodNode, new InsnNode(ICONST_2));
            BCG.addInstr(currentMethodNode, new InsnNode(IREM));
            BCG.addInstr(currentMethodNode, new JumpInsnNode(IFEQ, ifLabel));
            BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
            BCG.addInstr(currentMethodNode, ifLabel);
        } else {
            BCG.addInstr(currentMethodNode, whileLabel);
            evaluateExpression();
            Lexem operator = currentKeyword.lex;
            switch (operator) {
                case EQUAL:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPEQ, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
                    break;
                case NOT_EQUAL:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPNE, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
                    break;
                case LESS_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPLT, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
                    break;
                case LESS_OR_EQUAL_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPLE, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
                    break;
                case MORE_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPGT, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
                    break;
                case MORE_OR_EQUAL_THAN:
                    getNextKeyword();
                    evaluateExpression();
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(IF_ICMPGE, ifLabel));
                    BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, gotoLabel));
                    BCG.addInstr(currentMethodNode, ifLabel);
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
        ExpressionParser expressionParser = new ExpressionParser(currentExpression, identifierTable, currentMethodNode);
        expressionParser.parseExpression();
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
            BCG.addInstr(currentMethodNode, new VarInsnNode(ISTORE, Integer.parseInt(identifier.getAddress())));
            addPrintBytecodeCommands(identifier);

        } else if (isAccept(IdentifierCategory.CLASS_VAR)) {

            isExpect(Lexem.ASSIGN, 19);
            evaluateExpression();
            BCG.addInstr(currentMethodNode, new FieldInsnNode(PUTSTATIC, "ClassTest",
                    identifier.getName(), "I"));
            addPrintBytecodeCommands(identifier);

        } else if (isAccept(Command.CALL)) {
            BCG.addInstr(currentMethodNode, new MethodInsnNode(INVOKESTATIC, "ClassTest",
                    currentKeyword.word, "()V"));
            isExpect(Lexem.NAME, 14);
        } else if (isAccept(Command.BEGIN)) {
            do {
                statement();
            } while (isAccept(Lexem.SEMICOLON));
            isExpect(Command.END, 17);
        } else if (isAccept(Command.IF)) {
            condition();
            isExpect(Command.THEN, 16);
            statement();
            BCG.addInstr(currentMethodNode, gotoLabel);
        } else if (isAccept(Command.WHILE)) {
            condition();
            isExpect(Command.DO, 18);
            statement();
            BCG.addInstr(currentMethodNode, new JumpInsnNode(GOTO, whileLabel));
            BCG.addInstr(currentMethodNode, gotoLabel);
        } else {
            error(11);
            getNextKeyword();
        }
    }

    private void addPrintBytecodeCommands(Identifier identifier) {
        BCG.addInstr(currentMethodNode, new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        if (identifier.getCategory() == IdentifierCategory.LOCAL_VAR) {
            BCG.addInstr(currentMethodNode, new VarInsnNode(ILOAD, Integer.parseInt(identifier.getAddress())));
        }
        if (identifier.getCategory() == IdentifierCategory.CLASS_VAR) {
            BCG.addInstr(currentMethodNode, new FieldInsnNode(GETSTATIC, "ClassTest",
                    identifier.getName(), "I"));
        }

        BCG.addInstr(currentMethodNode, new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false));
    }


    private void updateIdentifierInfo(IdentifierCategory category, int level, String address) {
        identifierTable.getIdentifier(currentKeyword.word).setCategory(category);
        identifierTable.getIdentifier(currentKeyword.word).setLevel(level);
        identifierTable.getIdentifier(currentKeyword.word).setAddress(address);
    }

    @Override
    public boolean checkSyntax() {
        program();
        BCG.addInstr(currentMethodNode, new InsnNode(RETURN));
        return hasErrors;
    }
}
