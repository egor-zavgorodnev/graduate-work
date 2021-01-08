package ru.tver.tstu.backend;

import ru.tver.tstu.backend.exceptions.LexicalAnalyzeException;
import ru.tver.tstu.backend.model.Identifier;
import ru.tver.tstu.backend.model.Keyword;

import java.util.List;
import java.util.Set;

public interface INameTable {
    void recognizeAllIdentifiers(List<Keyword> keywords);
    Identifier getIdentifier(String name) throws LexicalAnalyzeException;
    Set<Identifier> getIdentifiers();
    void clear();
}
