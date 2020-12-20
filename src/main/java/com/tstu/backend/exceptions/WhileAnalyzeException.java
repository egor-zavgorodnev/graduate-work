package com.tstu.backend.exceptions;

public class WhileAnalyzeException extends RuntimeException {
    public WhileAnalyzeException(String message) {
        super("Не удалось разобрать цикл While: " + message);
    }
}
