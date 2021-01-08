package ru.tstu.tver.execution;

import ru.tstu.tver.backend.syntax.SyntaxParserWithBytecodeGen;
import ru.tstu.tver.backend.INameTable;
import ru.tstu.tver.backend.ISyntaxAnalyzer;
import ru.tstu.tver.backend.generator.bytecode.ByteCodeGenerator;
import ru.tstu.tver.backend.lexems.IdentifierTable;
import ru.tstu.tver.backend.lexems.LexicalAnalyzer;
import ru.tstu.tver.backend.model.Keyword;
import ru.tstu.tver.util.FileReader;

import java.util.List;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String data = FileReader.parseFromFile("src/main/resources/text.txt");
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
