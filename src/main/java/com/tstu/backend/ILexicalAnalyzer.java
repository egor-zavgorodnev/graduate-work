package com.tstu.backend;

import com.tstu.backend.lexical.Keyword;
import com.tstu.backend.lexical.LexicalAnalyzeException;

import java.util.List;

public interface ILexicalAnalyzer {
    List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException;
}
