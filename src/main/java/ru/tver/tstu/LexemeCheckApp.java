package ru.tver.tstu;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.util.FileReader;

public class LexemeCheckApp {

    public static void main(String[] args) throws LexicalAnalyzeException {
       String data = FileReader.parseFromSourceCodeFile("src/main/resources/nested-if.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        lexicalAnalyzer.recognizeAllLexem(data);
    }
}
