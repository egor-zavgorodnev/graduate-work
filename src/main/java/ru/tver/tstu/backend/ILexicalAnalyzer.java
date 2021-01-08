package ru.tver.tstu.backend;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.model.Keyword;

import java.util.List;

public interface ILexicalAnalyzer {
    List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException;
}
