package com.tstu.backend;

import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;

import java.util.List;
import java.util.Set;

public interface INameTable {
    void recognizeAllIdentifiers(List<Keyword> keywords);
    Identifier getIdentifier(String name);
    Set<Identifier> getIdentifiers();
}
