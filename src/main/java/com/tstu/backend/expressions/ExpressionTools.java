package com.tstu.backend.expressions;

import com.tstu.backend.model.Keyword;

import java.util.List;
import java.util.Stack;

public class ExpressionTools {

    private Stack<Operation> operationStack;
    private Stack<String> argumentStack;

    private List<Keyword> expression;

    public ExpressionTools(List<Keyword> expression) {
        this.expression = expression;
    }

    private void splitIntoStacks(List<Keyword> expression) {
        operationStack = new Stack<>();
        argumentStack = new Stack<>();

        for (Keyword keyword : expression) {
            switch (keyword.lex) {
                case NOT:
                    operationStack.add(new Operation(keyword.word, 3));
                    break;
                case AND:
                    operationStack.add(new Operation(keyword.word, 2));
                    break;
                case OR:
                case XOR:
                    operationStack.add(new Operation(keyword.word, 1));
                    break;
                case NAME:
                    argumentStack.push(keyword.word);
                    break;
            }
        }
    }

    public void parseExpression(List<Keyword> expression) {

    }
}
