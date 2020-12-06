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
                    continue;
                case '=':
                    addKeyword(currentSymbol, Lexems.EQUAL);
                    logger.info(symbols[i] + "(равно)");
                    continue;
                case '+':
                    addKeyword(currentSymbol, Lexems.ADDITION);
                    logger.info(symbols[i] + "(сложение)");
                    continue;
                case '-':
                    addKeyword(currentSymbol, Lexems.SUBSTRACTION);
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
                case '\n':
                    addKeyword(currentSymbol, Lexems.SPLITTER);
                    logger.info("\\n" + "(перенос строки)");
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

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.recognizeAllLexem("var a,b,c 0 1 :Logical\n");
    }

}
