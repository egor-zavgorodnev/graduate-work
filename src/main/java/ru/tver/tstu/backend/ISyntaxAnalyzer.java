package ru.tver.tstu.backend;


import ru.tver.tstu.backend.exceptions.*;

public interface ISyntaxAnalyzer {
    boolean checkSyntax() throws SyntaxAnalyzeException, ExpressionAnalyzeException, ConditionAnalyzeException, LexicalAnalyzeException, WhileAnalyzeException;
}
