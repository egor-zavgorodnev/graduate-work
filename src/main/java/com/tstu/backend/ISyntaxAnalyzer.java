package com.tstu.backend;

import com.tstu.backend.lexical.LexicalAnalyzeException;
import com.tstu.backend.syntax.SyntaxAnalyzeException;

public interface ISyntaxAnalyzer {
    void checkSyntax() throws SyntaxAnalyzeException, LexicalAnalyzeException;
}
