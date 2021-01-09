package ru.tver.tstu.execution;

import ru.tver.tstu.backend.syntax.SyntaxParserWithBytecodeGen;
import ru.tver.tstu.backend.INameTable;
import ru.tver.tstu.backend.ISyntaxAnalyzer;
import ru.tver.tstu.backend.generator.bytecode.ByteCodeGenerator;
import ru.tver.tstu.backend.lexems.IdentifierTable;
import ru.tver.tstu.backend.lexems.LexicalAnalyzer;
import ru.tver.tstu.backend.model.Keyword;
import ru.tver.tstu.util.FileReader;

import java.util.List;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String data = FileReader.parseFromFile("src/main/resources/text3.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ISyntaxAnalyzer syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable);
        syntaxAnalyzer.checkSyntax();

        Class<?> aClass = ByteCodeLoader.clazz.loadClass(ByteCodeGenerator.generateAsByteArray());

        ((Runnable) aClass.newInstance()).run(); //создаем класс и запускаем
    }
}
