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

            /* one symbol keywords */

            String currentSymbol = String.valueOf(symbols[i]);
            switch (symbols[i]) {
                case ' ':
                    continue;
                case '+':
                    addKeyword(currentSymbol, Lexems.PLUS);
                    logger.info(symbols[i] + "(сложение)");
                    continue;
                case '-':
                    addKeyword(currentSymbol, Lexems.MINUS);
                    logger.info(symbols[i] + "(вычитание)");
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
                addKeyword(number.toString(), Lexems.NUMBER);
                logger.info(number.toString() + "(число)");
                continue;
            }
            // =, ==
            if (symbols[i] == '=') {
                if (i == symbols.length - 1) {
                    addKeyword(String.valueOf(symbols[i]), Lexems.ASSIGN);
                    logger.info(symbols[i] + "(присваивание)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.EQUAL);
                    logger.info(twoSymbolsWord + "(равенство)");
                    i++;
                } else {
                    addKeyword(String.valueOf(symbols[i]), Lexems.ASSIGN);
                    logger.info(symbols[i] + "(присваивание)");
                }
                continue;
            }
            // >, >=
            if (symbols[i] == '>') {
                if (i == symbols.length - 1) {
                    addKeyword(String.valueOf(symbols[i]), Lexems.MORE);
                    logger.info(symbols[i] + "(больше)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.MOREOREQUAL);
                    logger.info(twoSymbolsWord + "(больше или равно)");
                    i++;
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
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexems.LESSOREQUAL);
                    logger.info(twoSymbolsWord + "(меньше или равно)");
                    i++;
                } else {
                    addKeyword(String.valueOf(symbols[i]), Lexems.LESS);
                    logger.info(symbols[i] + "(меньше)");
                }
                continue;
            }

            throw new LexicalAnalyzeException("Недопустимый символ");

        }

        return keywords;
    }

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.recognizeAllLexem(">=\n / + * == =");
    }

}
