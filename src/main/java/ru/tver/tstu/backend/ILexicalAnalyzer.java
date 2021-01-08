package ru.tstu.tver.backend;

import ru.tstu.tver.backend.exceptions.LexicalAnalyzeException;
import ru.tstu.tver.backend.model.Keyword;

import java.util.List;

public interface ILexicalAnalyzer {
    List<Keyword> recognizeAllLexem(String data) throws LexicalAnalyzeException;
}
