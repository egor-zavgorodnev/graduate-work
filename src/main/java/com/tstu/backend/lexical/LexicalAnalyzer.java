package com.tstu.backend.lexical;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Lexems;

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

            /* one symbol keywords */

            String currentSymbol = String.valueOf(symbols[i]);
            switch (symbols[i]) {
                case ' ':
                    continue;
                case '!':
                    addKeyword(currentSymbol, Lexems.NOT);
                    logger.info(symbols[i] + "(NOT)");
                    continue;
                case '&':
                    addKeyword(currentSymbol, Lexems.AND);
                    logger.info(symbols[i] + "(AND)");
                    continue;
                case '|':
                    addKeyword(currentSymbol, Lexems.OR);
                    logger.info(symbols[i] + "(OR)");
                    continue;
                case '^':
                    addKeyword(currentSymbol, Lexems.XOR);
                    logger.info(symbols[i] + "(XOR)");
                    continue;
                case ',':
                    addKeyword(currentSymbol, Lexems.SEMI);
                    logger.info(symbols[i] + "(запятая)");
                    continue;
                case '\n':
                    addKeyword(currentSymbol, Lexems.SPLITTER);
                    logger.info("\\n" + "(перенос строки)");
                    continue;
            }

            /* two symbol keywords */

            // identifiers
            if (Character.isLetter(symbols[i])) {
                StringBuilder identifier = new StringBuilder();
                while (Character.isLetter(symbols[i])) {
                    identifier.append(symbols[i]);
                    if (i == symbols.length - 1) break;
                    i++;
                }
                i--;
                addKeyword(identifier.toString(), getIdentifierLexem(identifier.toString()));
                logger.info(identifier.toString() + "(идентификатор)");
                continue;
            }
            //numbers
            if (Character.isDigit(symbols[i])) {
                StringBuilder number = new StringBuilder();
                while (Character.isDigit(symbols[i])) {
                    number.append(symbols[i]);
                    if (i == symbols.length - 1) break;
                    i++;
                }
                if (number.length() == 1) i--;
                if(!isIntMaxOrMinValue(number.toString()))
                    throw new LexicalAnalyzeException("Число вышло за граници допустимого значения");
                addKeyword(number.toString(), Lexems.NUMBER);
                logger.info(number.toString() + "(число)");
                continue;
            }
            // :, :=
            if (symbols[i] == ':') {
                if (i == symbols.length - 1) {
                    addKeyword(currentSymbol, Lexems.COLON);
                    logger.info(symbols[i] + "(двоеточие)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.ASSIGN);
                    logger.info(twoSymbolsWord + "(присваивание)");
                    i++;
                } else {
                    addKeyword(currentSymbol, Lexems.COLON);
                    logger.info(symbols[i] + "(двоеточие)");
                }
                continue;
            }
            throw new LexicalAnalyzeException("Недопустимый символ");
        }

        return keywords;
    }

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.recognizeAllLexem("var a,b,c :Logical\n");
        int a = 2-1;
    }

    private boolean isIntMaxOrMinValue(String number) {
        long num;
        try {
            num = Long.parseLong(number);
        } catch (Exception e) {
            return false;
        }
        return num > Integer.MIN_VALUE && num < Integer.MAX_VALUE;
    }
}
