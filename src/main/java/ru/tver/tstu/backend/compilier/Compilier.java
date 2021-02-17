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
        nameTable.recognizeAllIdentifiers(lexems);

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems,nameTable);

        if (!syntaxAnalyzer.checkSyntax()) {
            return false;
        }

        ByteCodeGenerator.generateAsFileByPath();
        //TODO exception handling
        return true;
    }
}
