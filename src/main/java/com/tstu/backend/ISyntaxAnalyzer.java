package com.tstu.backend;

import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.exceptions.SyntaxAnalyzeException;

public interface ISyntaxAnalyzer {
    void checkSyntax() throws SyntaxAnalyzeException, LexicalAnalyzeException;
}
