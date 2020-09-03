package com.tstu.backend.lexical;

import com.tstu.backend.ILexicalAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LexicalAnalyzer implements ILexicalAnalyzer {

    private Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());

    private List<Keyword> keywords;

    public LexicalAnalyzer() {
        keywords = new ArrayList<>();
    }

    private void addKeyword(String word, Lexems lexem) {
        Keyword keyword = new Keyword(word, lexem);
        keyword.word = word;
        keyword.lex = lexem;

        keywords.add(keyword);
    }

    private Lexems getIdentifierLexem(String identifier) {
        for (Keyword keyword : keywords) {
            if (keyword.word.equals(identifier)) {
                return keyword.lex;
            }
        }
        return Lexems.NAME;
    }

    @Override
    public List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException {
        char[] symbols = data.toCharArray();

        for (int i = 0; i < symbols.length; i++) {

            //spaces
            if (symbols[i] == ' ') {
                continue;
            }
            // identifiers
            if (Character.isLetter(symbols[i])) {
                StringBuilder identifier = new StringBuilder();
                while (Character.isLetter(symbols[i])) {
                    if (i == symbols.length - 1) break;
                    identifier.append(symbols[i]);
                    i++;
                }
                addKeyword(identifier.toString(), getIdentifierLexem(identifier.toString()));
                logger.info(identifier.toString() + "(идентификатор)");
                continue;
            }
            //numbers
            if (Character.isDigit(symbols[i])) {
                StringBuilder number = new StringBuilder();
                while (Character.isDigit(symbols[i])) {
                    if (i == symbols.length - 1) break;
                    number.append(symbols[i]);
                    i++;
                }
                addKeyword(number.toString(), Lexems.NUMBER);
                logger.info(number.toString() + "(число)");
                continue;
            }
            // >, >=
            if (symbols[i] == '>') {
                if (i == symbols.length - 1) {
                    addKeyword(String.valueOf(symbols[i]), Lexems.MORE);
                    logger.info(symbols[i] + "(больше)");
                }
                if (symbols[i + 1] == '=') {
                    addKeyword(String.valueOf(symbols[i] + symbols[i + 1]), Lexems.MOREOREQUAL);
                    logger.info(symbols[i] + "(больше или равно)");
                } else {
                    addKeyword(String.valueOf(symbols[i]), Lexems.MORE);
                    logger.info(symbols[i] + "(больше)");
                }
                continue;
            }
            // <, <=
            if (symbols[i] == '<') {
                if (i == symbols.length - 1) {
                    addKeyword(String.valueOf(symbols[i]), Lexems.LESS);
                    logger.info(symbols[i] + "(меньше)");
                }
                if (symbols[i + 1] == '=') {
                    addKeyword(String.valueOf(symbols[i] + symbols[i + 1]), Lexems.LESSOREQUAL);
                    logger.info(symbols[i] + "(меньше или равно)");
                } else {
                    addKeyword(String.valueOf(symbols[i]), Lexems.LESS);
                    logger.info(symbols[i] + "(меньше)");
                }
                continue;
            }
            if (symbols[i] == '+') {
                addKeyword(String.valueOf(symbols[i]), Lexems.PLUS);
                logger.info(symbols[i] + "(сложение)");
                continue;
            }
            if (symbols[i] == '-') {
                addKeyword(String.valueOf(symbols[i]), Lexems.MINUS);
                logger.info(symbols[i] + "(вычитание)");
                continue;
            }
            if (symbols[i] == '*') {
                addKeyword(String.valueOf(symbols[i]), Lexems.MULTIPLICATION);
                logger.info(symbols[i] + "(умножение)");
                continue;
            }
            if (symbols[i] == '/') {
                addKeyword(String.valueOf(symbols[i]), Lexems.DIVISION);
                logger.info(symbols[i] + "(деление)");
                continue;
            }

            //addKeyword(String.valueOf(symbols[i]), Lexems.getLexema(String.valueOf(symbols[i])));

            throw new LexicalAnalyzeException("Недопустимый символ");

        }

        return keywords;
    }

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.recognizeAllLexem("2 + 2");
    }

}
