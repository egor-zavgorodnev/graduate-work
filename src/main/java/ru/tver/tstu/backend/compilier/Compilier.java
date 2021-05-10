package ru.tver.tstu.backend.compilier;

import ru.tver.tstu.backend.generator.bytecode.*;
import ru.tver.tstu.backend.lexems.*;
import ru.tver.tstu.backend.model.*;
import ru.tver.tstu.backend.syntax.*;

import java.util.*;

/**
 * Класс с единственным методом compile, осуществляющий лексический, синтаксический анализ и генерацию кода.
 */
public class Compilier {

    /**
     * Лексический, синтаксический анализ и генерацию кода.
     * @param sourceCode исходный текст программы
     * @return успешо ли скомпилирована программа
     */
    public boolean compile(String sourceCode) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

        IdentifierTable nameTable = new IdentifierTable();
        List<Keyword> lexems = lexicalAnalyzer.recognizeAllLexem(sourceCode);

        if (lexems == null) {
            return false;
        }

        nameTable.recognizeAllIdentifiers(lexems);

        ByteCodeBuilder byteCodeBuilder = new ByteCodeBuilder();
        RecursiveDescentParser syntaxAnalyzer = new SyntaxParserWithBytecodeGen(lexems, nameTable, byteCodeBuilder);
        if (!syntaxAnalyzer.checkSyntax()) {
            return false;
        }

        ByteCodeGenerator byteCodeGenerator = new ByteCodeGenerator(byteCodeBuilder);

        byteCodeGenerator.generateAsFileByPath();

        return true;
    }
}
