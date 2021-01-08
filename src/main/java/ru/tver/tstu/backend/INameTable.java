package ru.tstu.tver.backend;

import ru.tstu.tver.backend.exceptions.LexicalAnalyzeException;
import ru.tstu.tver.backend.model.Identifier;
import ru.tstu.tver.backend.model.Keyword;

import java.util.List;
import java.util.Set;

public interface INameTable {
    void recognizeAllIdentifiers(List<Keyword> keywords);
    Identifier getIdentifier(String name) throws LexicalAnalyzeException;
    Set<Identifier> getIdentifiers();
    void clear();
}
