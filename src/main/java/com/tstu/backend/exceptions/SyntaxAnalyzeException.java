package com.tstu.backend.exceptions;

public class SyntaxAnalyzeException extends Exception {
    public SyntaxAnalyzeException(String message) {
        super("Синтаксическая ошибка : " + message);
    }
}
