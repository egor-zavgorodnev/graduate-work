package com.tstu.backend;

import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Keyword;

import java.util.List;

public interface ILexicalAnalyzer {
    List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException;
}
