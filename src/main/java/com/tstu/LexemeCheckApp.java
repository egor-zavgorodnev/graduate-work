package com.tstu;

import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.util.FileReader;

public class LexemeCheckApp {

    public static void main(String[] args) throws LexicalAnalyzeException {
       String data = FileReader.parseFromFile("src/main/resources/text.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        lexicalAnalyzer.recognizeAllLexem(data);
    }
}
