package com.tstu.backend.exceptions;

public class ConditionAnalyzeException extends Exception {
    public ConditionAnalyzeException(String message) {
        super("Не удалось разобрать условие :" + message);
    }
}
