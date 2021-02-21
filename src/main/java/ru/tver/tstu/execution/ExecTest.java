package ru.tver.tstu.execution;

import ru.tver.tstu.backend.syntax.*;
import ru.tver.tstu.backend.generator.bytecode.ByteCodeGenerator;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.util.FileReader;

import java.util.List;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String data = FileReader.parseFromSourceCodeFile("src/main/resources/text.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable);
        syntaxAnalyzer.checkSyntax();

        Class<?> aClass = ByteCodeLoader.clazz.loadClass(ByteCodeGenerator.generateAsByteArray());

        ((Runnable) aClass.newInstance()).run(); //создаем класс и запускаем
    }
}
