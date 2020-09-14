package com.tstu.backend.expressions;

import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.generator.CodeGenerator;
import com.tstu.backend.model.Argument;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.Operation;
import com.tstu.backend.model.enums.Lexems;
import com.tstu.backend.model.enums.tCat;
import com.tstu.util.CustomLogger;
import com.tstu.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {

    private static List<Argument<String>> arguments;

    private INameTable nameTable;
    private Stack<Operation> operationStack;
    private Stack<String> argumentStack;

    private List<Keyword> expression;

    private Logger logger = new CustomLogger(ExpressionParser.class.getName());

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
                    CodeGenerator.addInstruction("mov ax," + argumentStack.peek() + "b");
                    CodeGenerator.addInstruction("push ax");
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
                    logger.info("!" + arg1);
                    argumentStack.push("expr");
                    break;
                case OR:
                case XOR:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    CodeGenerator.addInstruction("pop bx");
                    CodeGenerator.addInstruction("pop ax");
                    CodeGenerator.addInstruction("or ax,bx");
                    CodeGenerator.addInstruction("push bx");
                    logger.info(arg1 + " | " + arg2);
                    argumentStack.push("expr");
                    break;
                case AND:
                    arg1 = argumentStack.pop();
                    arg2 = argumentStack.pop();
                    CodeGenerator.addInstruction("pop bx");
                    CodeGenerator.addInstruction("pop ax");
                    CodeGenerator.addInstruction("and ax,bx");
                    CodeGenerator.addInstruction("push bx");
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
            CodeGenerator.declareStackAndCodeSegments();
            arguments.forEach(a -> CodeGenerator.addInstruction("mov " + a.getVariable().getName() + "," + a.getValue() + "b"));
            calculateExpression();
            CodeGenerator.addInstruction("mov " + expression.get(0).word + ", ax");
        }

    }
}
