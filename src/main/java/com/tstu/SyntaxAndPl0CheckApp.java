package com.tstu;

import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.syntax.SyntaxParserWithPl0StackCodeGen;
import com.tstu.util.FileReader;

import java.util.List;

public class SyntaxAndPl0CheckApp {

    public static void main(String[] args) throws LexicalAnalyzeException {
        String data = FileReader.parseFromFile("src/main/resources/text.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ISyntaxAnalyzer syntaxAnalyzer = new SyntaxParserWithPl0StackCodeGen(lexems,nameTable);
        syntaxAnalyzer.checkSyntax();

    }
}
