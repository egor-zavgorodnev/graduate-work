package com.tstu;

import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.generator.bytecode.ByteCodeGenerator;
import com.tstu.backend.generator.pl0.PL0CodeGenerator;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.syntax.RecursiveDescentParser;
import com.tstu.util.FileReader;

import java.util.List;

public class ByteCodeCheckApp {
    public static void main(String[] args) throws LexicalAnalyzeException {
        String data = FileReader.parseFromFile("src/main/resources/text.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ISyntaxAnalyzer syntaxAnalyzer = new RecursiveDescentParser(lexems,nameTable);
        syntaxAnalyzer.checkSyntax();
        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(PL0CodeGenerator.getInstructions());

        byteCodeGenerator.generateAsFileByPath();
    }
}
