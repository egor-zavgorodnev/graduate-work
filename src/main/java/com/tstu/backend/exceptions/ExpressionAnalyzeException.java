package com.tstu.backend.exceptions;

public class ExpressionAnalyzeException extends Exception {
    public ExpressionAnalyzeException(String message) {
        super("Не удалось разобрать выражение :" + message);
    }
}
