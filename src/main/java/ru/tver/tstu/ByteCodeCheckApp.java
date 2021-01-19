package ru.tver.tstu;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.syntax.SyntaxParserWithBytecodeGen;
import ru.tver.tstu.backend.INameTable;
import ru.tver.tstu.backend.ISyntaxAnalyzer;
import ru.tver.tstu.backend.generator.bytecode.ByteCodeGenerator;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.util.FileReader;

import java.util.List;

public class ByteCodeCheckApp {
    public static void main(String[] args) throws LexicalAnalyzeException {
        String data = FileReader.parseFromFile("src/main/resources/tests/procedure/rec.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ISyntaxAnalyzer syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems,nameTable);
        syntaxAnalyzer.checkSyntax();

        ByteCodeGenerator.generateAsFileByPath();
    }
}
