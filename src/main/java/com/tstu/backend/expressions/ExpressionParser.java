package com.tstu.backend.expressions;

import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.model.Argument;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.Operation;
import com.tstu.backend.model.enums.Lexems;
import com.tstu.backend.model.enums.tCat;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

public class ExpressionParser {

    private static List<Argument<String>> arguments;

    private INameTable nameTable;
    private Stack<Operation> operationStack;
    private Stack<String> argumentStack;

    private List<Keyword> expression;

    private Logger logger = Logger.getLogger(ExpressionParser.class.getName());

    static {
        arguments = new ArrayList<>();
    }

    public ExpressionParser(List<Keyword> expression, INameTable nameTable) {
        this.expression = expression;
        this.nameTable = nameTable;
    }

    private void parseDeclaration() throws ExpressionAnalyzeException {
        if (nameTable.getIdentifier(expression.get(0).word).getCategory() != tCat.VAR) {
            throw new ExpressionAnalyzeException("Ожидается переменная");
        }
        if (expression.get(1).lex != Lexems.ASSIGN) {
            throw new ExpressionAnalyzeException("Ожидается присваивание");
        }
        Keyword value = expression.get(2);
        switch (value.lex) {
            case TRUE:
            case FALSE:
                arguments.add(new Argument<>(nameTable.getIdentifier(expression.get(0).word), String.valueOf(value.lex.getValue())));
                break;
            default:
                throw new ExpressionAnalyzeException("Ожидается значение переменной");
        }
    }

    private String getVariableValue(String word) {
        return arguments.stream().filter(v -> v.getVariable().getName().equals(word)).findFirst().get().getValue();
    }

    private void calculateExpression() throws ExpressionAnalyzeException {
        operationStack = new Stack<>();
        argumentStack = new Stack<>();

        if (nameTable.getIdentifier(expression.get(0).word).getCategory() != tCat.VAR) {
            throw new ExpressionAnalyzeException("Ожидается переменная");
        }
        if (expression.get(1).lex != Lexems.ASSIGN) {
            throw new ExpressionAnalyzeException("Ожидается присваивание");
        }

        for (int i = 2; i < expression.size(); i++) {
            Operation currentOperation;
            switch (expression.get(i).lex) {
                case NOT:
                    argumentStack.push("not");
                    currentOperation = new Operation(expression.get(i), 3);
                    calculateOperation(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    break;
                case AND:
                    currentOperation = new Operation(expression.get(i), 2);
                    calculateOperation(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    break;
                case OR:
                case XOR:
                    currentOperation = new Operation(expression.get(i), 1);
                    calculateOperation(currentOperation.getPriority());
                    operationStack.push(currentOperation);
                    break;
                case NAME:
                    argumentStack.push(getVariableValue(expression.get(i).word));
                    break;
            }
        }

        calculateOperation(0);

    }

    private void calculateOperation(int minPriority) {
        if (operationStack.size() == 0)
            return;

        Operation currentOperation = operationStack.peek();

        if (currentOperation.getPriority() >= minPriority) {
            String arg1;
            String arg2;
            currentOperation = operationStack.pop();
            switch (currentOperation.getSign().lex) {
                case NOT:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    logger.info( "!" + arg1);

                    argumentStack.push("expr");
                    break;
                case OR:
                case XOR:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();

                    logger.info(arg1 + " | " + arg2);
                    argumentStack.push("expr");
                    break;
                case AND:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();

                    logger.info(arg1 + " & " + arg2);
                    argumentStack.push("expr");
                    break;
            }

            if (currentOperation.getPriority() >= minPriority) {
                calculateOperation(minPriority);
            }

        }

    }

    public void parseExpression() throws ExpressionAnalyzeException {
        if (expression.size() == 4) {
            parseDeclaration();
        } else {
            calculateExpression();
        }
    }
}
