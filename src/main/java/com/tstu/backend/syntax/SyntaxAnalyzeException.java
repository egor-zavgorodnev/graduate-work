package com.tstu.backend.syntax;

public class SyntaxAnalyzeException extends Exception {
    public SyntaxAnalyzeException(String message) {
        super("Синтаксическая ошибка : " + message);
    }
}
