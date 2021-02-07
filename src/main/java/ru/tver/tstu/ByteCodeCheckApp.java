package ru.tver.tstu;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.syntax.*;
import ru.tver.tstu.backend.generator.bytecode.ByteCodeGenerator;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.util.FileReader;

import java.util.List;

public class ByteCodeCheckApp {
    public static void main(String[] args) throws LexicalAnalyzeException {
        String data = FileReader.parseFromFile("src/main/resources/tests/while/nested-while.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems,nameTable);
        syntaxAnalyzer.checkSyntax();

        ByteCodeGenerator.generateAsFileByPath();
    }
}
