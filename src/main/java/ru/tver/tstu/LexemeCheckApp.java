package ru.tver.tstu;

import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.util.*;

public class LexemeCheckApp {

    public static void main(String[] args) {
        String data = FileReader.parseFromSourceCodeFile("src/main/resources/nested-if.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        lexicalAnalyzer.recognizeAllLexem(data);
    }
}
