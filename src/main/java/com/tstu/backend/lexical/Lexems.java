package com.tstu.backend.lexical;

public enum Lexems {

    NONE(""), NAME("name"), NUMBER("int"), BEGIN("Begin"), END("End"),
    IF("if"), THEN("then"), MULTIPLICATION("*"), DIVISION("/"), PLUS("+"),
    MINUS("-"), EQUAL("&&"), LESS("<"), LESSOREQUAL("<="), MORE(">"),
    MOREOREQUAL(">="), SEMI("semi"), ASSIGN("assign"), LEFTBRACKET("LeftBracket"),
    EOF("EOF"), SPLITTER("||");

    private String lexema;

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

//    public static Lexems getLexema(String string) throws LexicalAnalyzeException {
//        switch (string)
//        {
//            case "":
//                break;
//                break;
//            default:  throw new LexicalAnalyzeException("Недопустимый символ");
//        }
//    }

}
