package ru.tver.tstu.backend.compilier;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.syntax.*;

import java.util.*;

public class Compilier {

    public boolean compile(String sourceCode) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(sourceCode);


        ByteCodeBuilder byteCodeBuilder = new ByteCodeBuilder();
        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable, byteCodeBuilder);
        syntaxAnalyzer.checkSyntax();

        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(byteCodeBuilder);

        byteCodeGenerator.generateAsFileByPath();

        return true;
    }
}
