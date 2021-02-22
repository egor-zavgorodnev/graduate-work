package ru.tver.tstu;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.syntax.*;
import ru.tver.tstu.util.*;

import java.util.*;

public class ByteCodeCheckApp {
    public static void main(String[] args) {
        String data = FileReader.parseFromSourceCodeFile("src/main/resources/tests/procedure/3-proc.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ByteCodeBuilder byteCodeBuilder = new ByteCodeBuilder();

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable, byteCodeBuilder);
        syntaxAnalyzer.checkSyntax();

        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(byteCodeBuilder);
        byteCodeGenerator.generateAsFileByPath();
    }
}
