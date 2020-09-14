package com.tstu.backend;

import com.tstu.backend.exceptions.ExpressionAnalyzeException;
import com.tstu.backend.exceptions.SyntaxAnalyzeException;

public interface ISyntaxAnalyzer {
    void checkSyntax() throws SyntaxAnalyzeException, ExpressionAnalyzeException;
}
