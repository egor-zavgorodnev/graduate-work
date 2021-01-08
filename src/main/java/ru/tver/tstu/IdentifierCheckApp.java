package ru.tver.tstu;

import ru.tver.tstu.backend.INameTable;
import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.util.FileReader;

public class IdentifierCheckApp {

    public static void main(String[] args) throws LexicalAnalyzeException {
       String data = FileReader.parseFromFile("src/main/resources/text3.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();

        nameTable.recognizeAllIdentifiers(lexicalAnalyzer.recognizeAllLexem(data));

    }

}
