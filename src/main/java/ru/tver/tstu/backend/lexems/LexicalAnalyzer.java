package ru.tver.tstu.backend.lexems;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.model.enums.Lexem;
import ru.tver.tstu.backend.model.Keyword;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

/**
 * Лексический анализ, разбитие исходного текста на токены
 */
public class LexicalAnalyzer {

    private Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());
            //= new CustomLogger(LexicalAnalyzer.class.getName());//

    private List<Keyword> keywords;

    public LexicalAnalyzer() {
        keywords = new ArrayList<>();
    }

    private void addKeyword(String word, Lexem lexem) {
        Keyword keyword = new Keyword(word, lexem);
        keyword.word = word;
        keyword.lex = lexem;

        keywords.add(keyword);
    }

    private Lexem getIdentifierLexem(String identifier) {
        for (Keyword keyword : keywords) {
            if (keyword.word.equals(identifier)) {
                return keyword.lex;
            }
        }
        return Lexem.NAME;
    }

    public List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException {
        logger.info("\n---Разбор лексем---\n");
        char[] symbols = data.toCharArray();
        boolean isExit = false;
        for (int i = 0; i < symbols.length; i++) {

            if (isExit) break;

            /* one symbol keywords */

            String currentSymbol = String.valueOf(symbols[i]);
            switch (symbols[i]) {
                case ' ':
                case '\n':
                case '\r':
                    continue;
                case '=':
                    addKeyword(currentSymbol, Lexem.EQUAL);
                    logger.info(symbols[i] + "(равно)");
                    continue;
                case '#':
                    addKeyword(currentSymbol, Lexem.NOT_EQUAL);
                    logger.info(symbols[i] + "(не равно)");
                    continue;
                case '+':
                    addKeyword(currentSymbol, Lexem.ADDITION);
                    logger.info(symbols[i] + "(сложение)");
                    continue;
                case '-':
                    addKeyword(currentSymbol, Lexem.SUBTRACTION);
                    logger.info(symbols[i] + "(вычитание)");
                    continue;
                case ',':
                    addKeyword(currentSymbol, Lexem.SEMI);
                    logger.info(symbols[i] + "(запятая)");
                    continue;
                case '*':
                    addKeyword(currentSymbol, Lexem.MULTIPLICATION);
                    logger.info(symbols[i] + "(умножение)");
                    continue;
                case '/':
                    addKeyword(currentSymbol, Lexem.DIVISION);
                    logger.info(symbols[i] + "(деление)");
                    continue;
                case '.':
                    addKeyword(currentSymbol, Lexem.DOT);
                    logger.info(symbols[i] + "(точка)");
                    continue;
                case ';':
                    addKeyword(currentSymbol, Lexem.SEMICOLON);
                    logger.info(symbols[i] + "(точка с запятой)");
                    continue;
                case '(':
                    addKeyword(currentSymbol, Lexem.LEFT_BRACKET);
                    logger.info(symbols[i] + "(левая скобка)");
                    continue;
                case ')':
                    addKeyword(currentSymbol, Lexem.RIGHT_BRACKET);
                    logger.info(symbols[i] + "(правая скобка)");
                    continue;
            }

            /* two symbol and more keywords */
            // identifiers
            if (Character.isLetter(symbols[i])) {

                StringBuilder identifier = new StringBuilder();
                while (Character.isLetter(symbols[i])) {
                    identifier.append(symbols[i]);
                    if (i == symbols.length - 1) {
                        isExit = true;
                        break;
                    }
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
                    addKeyword(currentSymbol, Lexem.LESS_THAN);
                    logger.info(symbols[i] + "(меньше)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexem.LESS_OR_EQUAL_THAN);
                    logger.info(twoSymbolsWord + "(меньше или равно)");
                    i++;
                } else {
                    addKeyword(currentSymbol, Lexem.LESS_THAN);
                    logger.info(symbols[i] + "(меньше)");
                }
                continue;
            }
            // >, >=
            if (symbols[i] == '>') {
                if (i == symbols.length - 1) {
                    addKeyword(currentSymbol, Lexem.MORE_THAN);
                    logger.info(symbols[i] + "(больше)");
                    continue;
                }
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexem.MORE_OR_EQUAL_THAN);
                    logger.info(twoSymbolsWord + "(больше или равно)");
                    i++;
                } else {
                    addKeyword(currentSymbol, Lexem.MORE_THAN);
                    logger.info(symbols[i] + "(больше)");
                }
                continue;
            }
            // :=
            if (symbols[i] == ':') {
                if (symbols[i + 1] == '=') {
                    String twoSymbolsWord = symbols[i] + String.valueOf(symbols[i + 1]);
                    addKeyword(twoSymbolsWord, Lexem.ASSIGN);
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
                addKeyword(number.toString(), Lexem.NUMBER);
                logger.info(number.toString() + "(число)");
                continue;
            }

            throw new LexicalAnalyzeException("Недопустимый символ : " + symbols[i]);
        }

        return keywords;
    }

}
