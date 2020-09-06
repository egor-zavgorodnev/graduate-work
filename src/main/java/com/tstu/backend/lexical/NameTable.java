package com.tstu.backend.lexical;

import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.INameTable;

import java.util.*;
import java.util.logging.Logger;

public class NameTable implements INameTable {

    private Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());

    private Set<Identifier> identifiers;

    public NameTable() {
        identifiers = new HashSet<>();
    }

    private void addIdentifier(String name, tCat category) {
        Identifier identifier = new Identifier();
        identifier.name = name;
        identifier.category = category;

        identifiers.add(identifier);
    }

    private void addIdentifier(String name, tCat category, tType type) {
        Identifier identifier = new Identifier();
        identifier.name = name;
        identifier.category = category;
        identifier.type = type;

        identifiers.add(identifier);
    }

    @Override
    public Optional<Identifier> getIdentifier(String name) {
        return identifiers.stream().filter(i -> i.name.equals(name)).findAny();
    }

    @Override
    public void recognizeAllIdentifiers(List<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            if (keyword.lex.equals(Lexems.NAME)) {
                if (EnumSet.allOf(tType.class).stream().anyMatch(e -> e.getName().equals(keyword.word))) {
                    addIdentifier(keyword.word, tCat.TYPE, tType.getTypeByName(keyword.word));
                    logger.info(keyword.word + "(тип данных)");
                } else if (EnumSet.allOf(Command.class).stream().anyMatch(e -> e.getName().equals(keyword.word))) {
                    addIdentifier(keyword.word, tCat.COMMAND);
                    logger.info(keyword.word + "(команда)");
                } else {
                    addIdentifier(keyword.word, tCat.VAR);
                    logger.info(keyword.word + "(переменная)");
                }
            }
        }

    }

    public static void main(String[] args) throws LexicalAnalyzeException {
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        INameTable nameTable = new NameTable();

        nameTable.recognizeAllIdentifiers(lexicalAnalyzer.recognizeAllLexem("Var a,b,c :Logical\n"));
    }

    @Override
    public Set<Identifier> getIdentifiers() {
        return identifiers;
    }
}
