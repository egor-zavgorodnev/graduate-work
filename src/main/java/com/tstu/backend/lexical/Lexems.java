package com.tstu.backend.lexical;

public enum Lexems {

    NONE(' '), NAME, NUMBER, NOT('!'), AND('&'), OR('|'), XOR('^'), ASSIGN(":="),
    SEMI(','), COLON(':'), SPLITTER('\n');

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
