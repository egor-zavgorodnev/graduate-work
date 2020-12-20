package com.tstu.backend.lexems;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Lexems;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer implements ILexicalAnalyzer {

    private Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());
            //= new CustomLogger(LexicalAnalyzer.class.getName());//

    private static List<Keyword> keywords;

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

    public List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException {
        logger.info("\n---Разбор лексем---\n");
        char[] symbols = data.toCharArray();

        for (int i = 0; i < symbols.length; i++) {

            /* one symbol keywords */

            String currentSymbol = String.valueOf(symbols[i]);
            switch (symbols[i]) {
                case ' ':
                case '\n':
                case '\r':
                    continue;
                case '=':
                    addKeyword(currentSymbol, Lexems.EQUAL);
                    logger.info(symbols[i] + "(равно)");
                    continue;
                case '#':
                    addKeyword(currentSymbol, Lexems.NOT_EQUAL);
                    logger.info(symbols[i] + "(не равно)");
                    continue;
                case '+':
                    addKeyword(currentSymbol, Lexems.ADDITION);
                    logger.info(symbols[i] + "(сложение)");
                    continue;
                case '-':
                    addKeyword(currentSymbol, Lexems.SUBTRACTION);
                    logger.info(symbols[i] + "(вычитание)");
                    continue;
                case ',':
                    addKeyword(currentSymbol, Lexems.SEMI);
                    logger.info(symbols[i] + "(запятая)");
                    continue;
                case '*':
                    addKeyword(currentSymbol, Lexems.MULTIPLICATION);
                    logger.info(symbols[i] + "(умножение)");
                    continue;
                case '/':
                    addKeyword(currentSymbol, Lexems.DIVISION);
                    logger.info(symbols[i] + "(деление)");
                    continue;
                case '.':
                    addKeyword(currentSymbol, Lexems.DOT);
                    logger.info(symbols[i] + "(точка)");
                    continue;
                case ';':
                    addKeyword(currentSymbol, Lexems.SEMICOLON);
                    logger.info(symbols[i] + "(точка с запятой)");
                    continue;
                case '(':
                    addKeyword(currentSymbol, Lexems.LEFT_BRACKET);
                    logger.info(symbols[i] + "(левая скобка)");
                    continue;
                case ')':
                    addKeyword(currentSymbol, Lexems.RIGHT_BRACKET);
                    logger.info(symbols[i] + "(правая скобка)");
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
            // <, <=
            if (symbols[i] == '<') {
                if (i == symbols.length - 1) {
                    addKeyword(currentSymbol, Lexems.LESS_THAN);
                    logger.info(symbols[i] + "(меньше)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.LESS_OR_EQUAL_THAN);
                    logger.info(twoSymbolsWord + "(меньше или равно)");
                    i++;
                } else {
                    addKeyword(currentSymbol, Lexems.LESS_THAN);
                    logger.info(symbols[i] + "(меньше)");
                }
                continue;
            }
            // >, >=
            if (symbols[i] == '>') {
                if (i == symbols.length - 1) {
                    addKeyword(currentSymbol, Lexems.MORE_THAN);
                    logger.info(symbols[i] + "(больше)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.MORE_OR_EQUAL_THAN);
                    logger.info(twoSymbolsWord + "(больше или равно)");
                    i++;
                } else {
                    addKeyword(currentSymbol, Lexems.MORE_THAN);
                    logger.info(symbols[i] + "(больше)");
                }
                continue;
            }
            // :=
            if (symbols[i] == ':') {
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.ASSIGN);
                    logger.info(twoSymbolsWord + "(присваивание)");
                    i++;
                    continue;
                }
            }
            // numbers
            if (Character.isDigit(symbols[i]))
            {
                StringBuilder number = new StringBuilder();
                while (Character.isDigit(symbols[i]))
                {
                    number.append(symbols[i]);
                    if (i == symbols.length - 1) break;
                    i++;
                }
                i--;
                addKeyword(number.toString(), Lexems.NUMBER);
                logger.info(number.toString() + "(число)");
                continue;
            }

            throw new LexicalAnalyzeException("Недопустимый символ : " + symbols[i]);
        }

        return keywords;
    }

}
