package com.tstu.backend.exceptions;

public class WhileAnalyzeException extends Exception {
    public WhileAnalyzeException(String message) {
        super("Не удалось разобрать цикл While: " + message);
    }
}
