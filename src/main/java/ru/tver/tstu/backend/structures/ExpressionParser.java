package ru.tver.tstu.backend.structures;


import org.objectweb.asm.tree.*;
import ru.tver.tstu.backend.generator.bytecode.ByteCodeBuilder;
import ru.tver.tstu.backend.generator.pl0.PL0CodeGenerator;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.Identifier;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.backend.model.Operation;
import ru.tver.tstu.backend.model.enums.IdentifierCategory;
import ru.tver.tstu.backend.model.enums.Lexem;
import ru.tver.tstu.backend.model.enums.OpCode;
import ru.tver.tstu.util.*;


import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public class ExpressionParser {

    private static final String SPECIAL_KEYWORD_NAME = "expr";

    private IdentifierTable nameTable;
    private MethodNode currentMethodNode;
    private Stack<Operation> operationStack;
    private Stack<Keyword> argumentStack;

    private List<Keyword> expression;

    private final Logger logger = new CustomLogger(ExpressionParser.class.getName());
    private boolean hasErrors = false;
    private final ByteCodeBuilder byteCodeBuilder;


    public ExpressionParser(List<Keyword> expression, IdentifierTable nameTable, MethodNode currentMethodNode, ByteCodeBuilder byteCodeBuilder) {
        this.expression = expression;
        this.nameTable = nameTable;
        this.currentMethodNode = currentMethodNode;
        this.byteCodeBuilder = byteCodeBuilder;
    }

    private void parseDeclaration() {

        Keyword sourceVariable = expression.get(0);
        Identifier currentIdentifier = nameTable.getIdentifier(expression.get(0).word);
        switch (sourceVariable.lex) {
            case NUMBER:
                 //logger.info("Присваивание - " + expression.get(0).word + " = " + sourceVariable.word);
                PL0CodeGenerator.addInstruction(OpCode.LIT, 0, expression.get(0).word);
                byteCodeBuilder.addInstruction(currentMethodNode, new LdcInsnNode(Integer.parseInt(expression.get(0).word)));
                break;
            case NAME:
                if (nameTable.getIdentifier(sourceVariable.word).getCategory() == IdentifierCategory.LOCAL_VAR) {
                     //logger.info("Присваивание - " + expression.get(0).word + " = " + sourceVariable.word);
                    PL0CodeGenerator.addInstruction(OpCode.LOD, currentIdentifier.getLevel(), currentIdentifier.getAddress());
                    byteCodeBuilder.addInstruction(currentMethodNode, new VarInsnNode(ILOAD, Integer.parseInt(currentIdentifier.getAddress())));
                    break;
                }
                if (nameTable.getIdentifier(sourceVariable.word).getCategory() == IdentifierCategory.CLASS_VAR) {
                     //logger.info("Присваивание - " + expression.get(0).word + " = " + sourceVariable.word);
                    PL0CodeGenerator.addInstruction(OpCode.LOD, currentIdentifier.getLevel(), currentIdentifier.getAddress());
                    byteCodeBuilder.addInstruction(currentMethodNode, new FieldInsnNode(GETSTATIC, "ClassTest",
                            currentIdentifier.getName(), "I"));
                    break;
                }
                logger.error("Ожидается значение переменной");
                hasErrors = true;
                break;
            default:
                logger.error("Ожидается значение переменной");
                hasErrors = true;
                break;
        }
    }

    private void calculateExpression() {
        operationStack = new Stack<>();
        argumentStack = new Stack<>();

        int depth = 0;
        boolean needValue = true;
        for (int i = 0; i < expression.size(); i++) {
            Operation currentOperation;
            switch (expression.get(i).lex) {
                case ADDITION:
                case SUBTRACTION:
                    if (needValue) {
                        logger.error("Ожидается операция");
                        hasErrors = true;
                    }
                    currentOperation = new Operation(expression.get(i), 1 + depth);
                    calculateOperations(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    needValue = true;
                    break;
                case MULTIPLICATION:
                case DIVISION:
                    if (needValue) {
                        logger.error("Ожидается операция");
                        hasErrors = true;
                    }
                    currentOperation = new Operation(expression.get(i), 2 + depth);
                    calculateOperations(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    needValue = true;
                    break;
                case NAME:
                    //is variable
                    if (nameTable.getIdentifier(expression.get(i).word).getCategory() == IdentifierCategory.LOCAL_VAR
                            || nameTable.getIdentifier(expression.get(i).word).getCategory() == IdentifierCategory.CLASS_VAR) {
                        if (!needValue) {
                            logger.error("Ожидается значение переменной или число");
                        }
                        argumentStack.push(expression.get(i));
                        needValue = false;
                        break;
                    }
                    logger.error("Ожидается значение переменной или число");
                    hasErrors = true;
                    break;
                case NUMBER:
                    if (!needValue) {
                        logger.error("Ожидается значение переменной или число");
                        hasErrors = true;
                    }
                    argumentStack.push(expression.get(i));
                    needValue = false;
                    break;
                case LEFT_BRACKET:
                    depth = 10;
                    break;
                case RIGHT_BRACKET:
                    // depth = 0;
                    break;
            }
        }
        calculateOperations(0);

    }

    private void calculateOperation(Keyword arg1, Keyword arg2, String operation, int instructionCode) {

        for (Keyword arg : List.of(arg2, arg1)) {
            Identifier identifier = nameTable.getIdentifier(arg.word);
            if (arg.lex == Lexem.NAME) {
                if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
                    //do nothing
                } else {
                    PL0CodeGenerator.addInstruction(OpCode.LOD, 1, identifier.getAddress());
                    if (identifier.getCategory() == IdentifierCategory.LOCAL_VAR) {
                        byteCodeBuilder.addInstruction(currentMethodNode, new VarInsnNode(ILOAD, Integer.parseInt(identifier.getAddress())));
                    }
                    if (identifier.getCategory() == IdentifierCategory.CLASS_VAR) {
                        byteCodeBuilder.addInstruction(currentMethodNode, new FieldInsnNode(GETSTATIC, "ClassTest",
                                identifier.getName(), "I"));
                    }

                }
            } else {
                PL0CodeGenerator.addInstruction(OpCode.LIT, 1, arg.word);
                byteCodeBuilder.addInstruction(currentMethodNode, new LdcInsnNode(Integer.parseInt(arg.word)));
            }
        }
        PL0CodeGenerator.addInstruction(OpCode.OPR, 1, operation);
        byteCodeBuilder.addInstruction(currentMethodNode, new InsnNode(instructionCode));
        argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
    }

    private void calculateOperations(int minPriority) {
        if (operationStack.size() == 0)
            return;

        Operation currentOperation = operationStack.peek();

        if (currentOperation.getPriority() >= minPriority) {
            currentOperation = operationStack.pop();
            switch (currentOperation.getSign().lex) {
                case ADDITION:
                    calculateOperation(argumentStack.pop(), argumentStack.pop(), "+", IADD);
                    break;
                case SUBTRACTION:
                    calculateOperation(argumentStack.pop(), argumentStack.pop(), "-", ISUB);
                    break;
                case MULTIPLICATION:
                    calculateOperation(argumentStack.pop(), argumentStack.pop(), "*", IMUL);
                    break;
                case DIVISION:
                    calculateOperation(argumentStack.pop(), argumentStack.pop(), "/", IDIV);
                    break;

            }

            if (currentOperation.getPriority() >= minPriority) {
                calculateOperations(minPriority);
            }

        }

    }

    public boolean parseExpression() {
        if (expression.size() <= 1) {
            parseDeclaration();
        } else {
            StringBuilder expr = new StringBuilder();
            expression.forEach(e -> expr.append(e.word));
            logger.info("\nРазбор выражения - " + expr);
            calculateExpression();
        }

        return hasErrors;
    }
}
