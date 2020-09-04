package com.tstu.backend.lexical;

public enum Lexems {

    NAME, NUMBER, MULTIPLICATION("*"), DIVISION("/"), PLUS("+"),
    MINUS("-"), EQUAL("=="), LESS("<"), LESSOREQUAL("<="), MORE(">"),
    MOREOREQUAL(">="), ASSIGN("="),
    EOF("EOF"), SPLITTER("\n");

    private String lexema;

    Lexems() {
    }

    Lexems(String lexema) {
        this.lexema = lexema;
    }

    public static Lexems getLexema(String string) throws LexicalAnalyzeException {
        try {
            return Lexems.valueOf(string);
        } catch (Exception e) {
            throw new LexicalAnalyzeException("Недопустимый символ");
        }
    }

}
