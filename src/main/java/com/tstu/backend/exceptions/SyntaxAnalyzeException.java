package com.tstu.backend.exceptions;

public class SyntaxAnalyzeException extends RuntimeException {
    public SyntaxAnalyzeException(String message) {
        super("Синтаксическая ошибка : " + message);
    }
}
