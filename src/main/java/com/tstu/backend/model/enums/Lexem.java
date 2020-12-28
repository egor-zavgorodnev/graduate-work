package com.tstu.backend.model.enums;

public enum Lexem {

    NAME,
    NUMBER,
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    ASSIGN(":="),
    EQUAL("="),
    NOT_EQUAL("#"),
    LESS_THAN("<"),
    MORE_THAN(">"),
    LESS_OR_EQUAL_THAN("<="),
    MORE_OR_EQUAL_THAN(">="),
    DOT("."),
    SEMI(","),
    SEMICOLON(";"),
    LEFT_BRACKET("("),
    RIGHT_BRACKET(")");

    Lexem(String value) {
    }

    Lexem() {
    }
}
