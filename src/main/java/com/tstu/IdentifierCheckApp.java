package com.tstu;

import com.tstu.backend.INameTable;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.util.FileReader;

public class IdentifierCheckApp {

    public static void main(String[] args) throws LexicalAnalyzeException {
       String data = FileReader.parseFromFile("src/main/resources/text3.txt");

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();

        nameTable.recognizeAllIdentifiers(lexicalAnalyzer.recognizeAllLexem(data));

    }

}
