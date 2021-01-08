package ru.tstu.tver.backend.exceptions;

public class ExpressionAnalyzeException extends RuntimeException{
    public ExpressionAnalyzeException(String message) {
        super("Не удалось разобрать выражение :" + message);
    }
}
