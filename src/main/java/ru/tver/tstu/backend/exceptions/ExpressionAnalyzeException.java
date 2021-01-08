package ru.tver.tstu.backend.exceptions;

public class ExpressionAnalyzeException extends RuntimeException{
    public ExpressionAnalyzeException(String message) {
        super("Не удалось разобрать выражение :" + message);
    }
}
