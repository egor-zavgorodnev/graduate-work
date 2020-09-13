package com.tstu.backend.lexems;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Lexems;
import com.tstu.util.CustomLogger;
import com.tstu.util.Logger;


import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer implements ILexicalAnalyzer {

    private Logger logger = new CustomLogger(LexicalAnalyzer.class.getName());//Logger.getLogger(LexicalAnalyzer.class.getName());

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
                case '0':
                    addKeyword(currentSymbol, Lexems.FALSE);
                    logger.info(symbols[i] + "(ложь)");
                    continue;
                case '1':
                    addKeyword(currentSymbol, Lexems.TRUE);
                    logger.info(symbols[i] + "(истина)");
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
            if (Character.isDigit(symbols[i])) {
                throw new LexicalAnalyzeException("Недопустимый символ: Из цифр только 0 или 1 (логическое значение)");
            }
            throw new LexicalAnalyzeException("Недопустимый символ : " + symbols[i]);
        }

        return keywords;
    }

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.recognizeAllLexem("var a,b,c 0 1 :Logical\n");
    }

}
