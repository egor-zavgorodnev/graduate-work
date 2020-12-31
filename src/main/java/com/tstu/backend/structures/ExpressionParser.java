package com.tstu.backend.structures;

import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.generator.pl0.PL0CodeGenerator;
import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.Operation;
import com.tstu.backend.model.enums.Function;
import com.tstu.backend.model.enums.IdentifierCategory;
import com.tstu.backend.model.enums.Lexem;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Stack;

public class ExpressionParser {

    private static final String SPECIAL_KEYWORD_NAME = "expr";

    private INameTable nameTable;
    private Stack<Operation> operationStack;
    private Stack<Keyword> argumentStack;

    private List<Keyword> expression;

    private Logger logger = Logger.getLogger(ExpressionParser.class.getName());
    //= new CustomLogger(ExpressionParser.class.getName());

    public ExpressionParser(List<Keyword> expression, INameTable nameTable) {
        this.expression = expression;
        this.nameTable = nameTable;
    }

    private void parseDeclaration() throws ExpressionAnalyzeException, LexicalAnalyzeException {

        Keyword sourceVariable = expression.get(0);
        Identifier currentIdentifier = nameTable.getIdentifier(expression.get(0).word);
        switch (sourceVariable.lex) {
            case NUMBER:
                // logger.info("Присваивание - " + expression.get(0).word + " = " + sourceVariable.word);
                PL0CodeGenerator.addInstruction(Function.LIT, 0, expression.get(0).word);
                break;
            case NAME:
                if (nameTable.getIdentifier(sourceVariable.word).getCategory() == IdentifierCategory.VAR) {
                    // logger.info("Присваивание - " + expression.get(0).word + " = " + sourceVariable.word);
                    PL0CodeGenerator.addInstruction(Function.LOD, currentIdentifier.getLevel(), currentIdentifier.getAddress());
                }
                break;
            default:
                throw new ExpressionAnalyzeException("Ожидается значение переменной");
        }
    }

    private void calculateExpression() throws ExpressionAnalyzeException, LexicalAnalyzeException {
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
                        throw new ExpressionAnalyzeException("Ожидается операция");
                    }
                    currentOperation = new Operation(expression.get(i), 1 + depth);
                    calculateOperations(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    needValue = true;
                    break;
                case MULTIPLICATION:
                case DIVISION:
                    if (needValue) {
                        throw new ExpressionAnalyzeException("Ожидается операция");
                    }
                    currentOperation = new Operation(expression.get(i), 2 + depth);
                    calculateOperations(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    needValue = true;
                    break;
                case NAME:
                case NUMBER:
                    if (!needValue) {
                        throw new ExpressionAnalyzeException("Ожидается значение");
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

//    private void calculateOperation(Keyword arg1, Keyword arg2, Keyword sign) {
//        arg1 = argumentStack.pop();
//        arg2 = argumentStack.pop();
//        for (Keyword arg : List.of(arg1, arg2)) {
//            if (arg.lex == Lexem.NAME) {
//                if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
//                    //do nothing
//                } else {
//                    PL0CodeGenerator.addInstruction(Function.LOD, 1, nameTable.getIdentifier(arg.word).getAddress());
//                }
//            } else {
//                PL0CodeGenerator.addInstruction(Function.LIT, 1, arg.word);
//            }
//        }
//        PL0CodeGenerator.addInstruction(Function.OPR, 1, "+");
//        argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
//    }

    private void calculateOperations(int minPriority) {
        if (operationStack.size() == 0)
            return;

        Operation currentOperation = operationStack.peek();

        if (currentOperation.getPriority() >= minPriority) {
            Keyword arg1;
            Keyword arg2;
            currentOperation = operationStack.pop();
            switch (currentOperation.getSign().lex) {
                case ADDITION:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    for (Keyword arg : List.of(arg1, arg2)) {
                        if (arg.lex == Lexem.NAME) {
                            if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
                                //do nothing
                            } else {
                                PL0CodeGenerator.addInstruction(Function.LOD, 1, nameTable.getIdentifier(arg.word).getAddress());
                            }
                        } else {
                            PL0CodeGenerator.addInstruction(Function.LIT, 1, arg.word);
                        }
                    }
                    PL0CodeGenerator.addInstruction(Function.OPR, 1, "+");
                    argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
                    break;
                case SUBTRACTION:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    for (Keyword arg : List.of(arg1, arg2)) {
                        if (arg.lex == Lexem.NAME) {
                            if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
                                //do nothing
                            } else {
                                PL0CodeGenerator.addInstruction(Function.LOD, 1, nameTable.getIdentifier(arg.word).getAddress());
                            }
                        } else {
                            PL0CodeGenerator.addInstruction(Function.LIT, 1, arg.word);
                        }
                    }
                    PL0CodeGenerator.addInstruction(Function.OPR, 1, "-");
                    argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
                    break;
                case MULTIPLICATION:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    for (Keyword arg : List.of(arg1, arg2)) {
                        if (arg.lex == Lexem.NAME) {
                            if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
                                //do nothing
                            } else {
                                PL0CodeGenerator.addInstruction(Function.LOD, 1, nameTable.getIdentifier(arg.word).getAddress());
                            }
                        } else {
                            PL0CodeGenerator.addInstruction(Function.LIT, 1, arg.word);
                        }
                    }
                    PL0CodeGenerator.addInstruction(Function.OPR, 1, "*");
                    argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
                    break;
                case DIVISION:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    for (Keyword arg : List.of(arg1, arg2)) {
                        if (arg.lex == Lexem.NAME) {
                            if (arg.word.equals(SPECIAL_KEYWORD_NAME)) {
                                //do nothing
                            } else {
                                PL0CodeGenerator.addInstruction(Function.LOD, 1, nameTable.getIdentifier(arg.word).getAddress());
                            }
                        } else {
                            PL0CodeGenerator.addInstruction(Function.LIT, 1, arg.word);
                        }
                    }
                    PL0CodeGenerator.addInstruction(Function.OPR, 1, "/");
                    argumentStack.push(new Keyword(SPECIAL_KEYWORD_NAME, Lexem.NAME)); // special kw for addition in stack
                    break;

            }

            if (currentOperation.getPriority() >= minPriority) {
                calculateOperations(minPriority);
            }

        }

    }

    public void parseExpression() throws ExpressionAnalyzeException, LexicalAnalyzeException {
        if (expression.size() <= 1) {
            parseDeclaration();
        } else {
            StringBuilder expr = new StringBuilder();
            expression.forEach(e -> expr.append(e.word));
            //logger.info("\nРазбор выражения - " + expr);
            calculateExpression();
            //CodeGenerator.addInstruction("mov " + expression.get(0).word + ", ax");
        }
    }
}
