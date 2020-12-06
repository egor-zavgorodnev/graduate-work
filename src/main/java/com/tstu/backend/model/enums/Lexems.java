package com.tstu.backend.model.enums;

public enum Lexems {

    NAME, NUMBER,ADDITION('+'), SUBSTRACTION('-'), MULTIPLICATION('*'), DIVISION('/'), ASSIGN(":="),
    EQUAL('='),NOT_EQUAL("!="), SEMI(','), COLON(':'), SPLITTER('\n'), LEFT_BRACKET('('), RIGHT_BRACKET(')');

    private char value;
    private String stringValue;

    Lexems() {
    }

    Lexems(String stringValue) {
        this.stringValue = stringValue;
    }

    Lexems(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }
}
