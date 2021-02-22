package ru.tver.tstu.execution;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.syntax.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Executor {

    private static final String CLASS_FILE_PATH = "file.class"; //root dir;


    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(args[0]);
        nameTable.recognizeAllIdentifiers(lexems);

        ByteCodeBuilder byteCodeBuilder = new ByteCodeBuilder();

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable, byteCodeBuilder);
        syntaxAnalyzer.checkSyntax();

        Class<?> aClass = ByteCodeLoader.clazz.loadClass(Files.readAllBytes(Path.of(CLASS_FILE_PATH)));

        ((Runnable) aClass.newInstance()).run();
    }
/*    public static void execute(String sourceCode) throws IllegalAccessException, InstantiationException, IOException {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(sourceCode);
        nameTable.recognizeAllIdentifiers(lexems);

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable);
        syntaxAnalyzer.checkSyntax();

        Class<?> aClass = ByteCodeLoader.clazz.loadClass(Files.readAllBytes(Path.of(CLASS_FILE_PATH)));

        ((Runnable) aClass.newInstance()).run();
    }*/
}
