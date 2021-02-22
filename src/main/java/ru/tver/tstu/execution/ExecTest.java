package ru.tver.tstu.execution;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.syntax.*;
import ru.tver.tstu.util.*;

import java.util.*;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ByteCodeLoader.clear();

        String data = FileReader.parseFromSourceCodeFile("src/main/resources/tests/expr/all-op-12171.6.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ByteCodeBuilder byteCodeBuilder = new ByteCodeBuilder();

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable, byteCodeBuilder);
        syntaxAnalyzer.checkSyntax();

        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(byteCodeBuilder);
        Class<?> aClass = ByteCodeLoader.clazz.loadClass(byteCodeGenerator.generateAsByteArray());

        ((Runnable) aClass.newInstance()).run(); //создаем класс и запускаем

    }
}
