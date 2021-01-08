package ru.tver.tstu.backend.exceptions;

public class ConditionAnalyzeException extends RuntimeException {
    public ConditionAnalyzeException(String message) {
        super("Не удалось разобрать условие :" + message);
    }
}
