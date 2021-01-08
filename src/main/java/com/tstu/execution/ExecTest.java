package com.tstu.execution;

import com.tstu.backend.INameTable;
import com.tstu.backend.ISyntaxAnalyzer;
import com.tstu.backend.generator.bytecode.ByteCodeGenerator;
import com.tstu.backend.generator.pl0.PL0CodeGenerator;
import com.tstu.backend.lexems.IdentifierTable;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.syntax.SyntaxParserWithBytecodeGen;
import com.tstu.util.FileReader;

import java.util.List;

public class ExecTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        String data = FileReader.parseFromFile("src/main/resources/text.txt");
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        INameTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(data);
        nameTable.recognizeAllIdentifiers(lexems);

        ISyntaxAnalyzer syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems,nameTable);
        syntaxAnalyzer.checkSyntax();
        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(PL0CodeGenerator.getInstructions());

       // byteCodeGenerator.generateAsFileByPath();
        Class<?> aClass = ByteCodeLoader.clazz.loadClass(byteCodeGenerator.generateAsByteArray());

        ((Runnable)aClass.newInstance()).run(); //создаем класс и запускаем
    }
}
