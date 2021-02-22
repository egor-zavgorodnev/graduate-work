package ru.tver.tstu.execution;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.syntax.*;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.util.FileReader;

import java.util.List;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String data = FileReader.parseFromSourceCodeFile("src/main/resources/tests/while/while-if.txt");
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
