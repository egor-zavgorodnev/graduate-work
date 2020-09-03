package com.tstu.backend.lexical;

import com.tstu.backend.ILexicalAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer implements ILexicalAnalyzer {

    private Logger logger = LoggerFactory.getLogger(LexicalAnalyzer.class);

    private String data;
    private List<Keyword> keywords;

    public LexicalAnalyzer(String data) {
        this.data = data;
        keywords = new ArrayList<>();
    }

    private void addKeyword(String word, Lexems lexem) {
        Keyword keyword = new Keyword(word, lexem);
        keyword.word = word;
        keyword.lex = lexem;

        keywords.add(keyword);
    }


    @Override
    public List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException {
        char[] symbols = data.toCharArray();

        for (int i = 0; i < symbols.length; i++) {

            String oneSymbolWord = String.valueOf(symbols[i]);
            String twoSymbolWord = String.valueOf(symbols[i] + symbols[i + 1]);

            //spaces
            if (symbols[i] == ' ') {
                continue;
            }
            // identifiers
            if (Character.isLetter(symbols[i])) {
                StringBuilder identifier = new StringBuilder();
                while (Character.isLetter(symbols[i])) {
                    identifier.append(symbols[i]);
                    i++;
                }
                //TODO Method getKeyWord from doc
                addKeyword(identifier.toString(), Lexems.NAME);
                continue;
            }
            //numbers
            if (Character.isDigit(symbols[i])) {
                StringBuilder number = new StringBuilder();
                while (Character.isDigit(symbols[i])) {
                    number.append(symbols[i]);
                    i++;
                }
                //TODO Method getKeyWord from doc
                addKeyword(number.toString(), Lexems.NUMBER);
                continue;
            }
            // >, >=
            if (symbols[i] == '>') {
                if (symbols[i + 1] == '=') {
                    addKeyword(twoSymbolWord, Lexems.MOREOREQUAL);
                } else {
                    addKeyword(oneSymbolWord, Lexems.MORE);
                }
                continue;
            }
            // <, <=
            if (symbols[i] == '<') {
                if (symbols[i + 1] == '=') {
                    addKeyword(twoSymbolWord, Lexems.LESSOREQUAL);
                } else {
                    addKeyword(oneSymbolWord, Lexems.LESS);
                }
                continue;
            }
//            if (symbols[i] == '+') {
//                addKeyword(oneSymbolWord, Lexems.PLUS);
//                continue;
//            }
//            if (symbols[i] == '-') {
//                addKeyword(oneSymbolWord, Lexems.MINUS);
//                continue;
//            }
//            if (symbols[i] == '*') {
//                addKeyword(oneSymbolWord, Lexems.MULTIPLICATION);
//                continue;
//            }
//            if (symbols[i] == '/') {
//                addKeyword(oneSymbolWord, Lexems.DIVISION);
//                continue;
//            }

            addKeyword(oneSymbolWord,Lexems.getLexema(String.valueOf(symbols[i])));

            throw new LexicalAnalyzeException("Недопустимый символ");

        }

        return keywords;
    }

}
