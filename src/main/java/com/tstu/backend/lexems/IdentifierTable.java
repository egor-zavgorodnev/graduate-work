package com.tstu.backend.lexems;

import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.model.Identifier;
import com.tstu.backend.model.Keyword;
import com.tstu.backend.model.enums.Command;
import com.tstu.backend.model.enums.Lexems;
import com.tstu.backend.model.enums.tCat;
import com.tstu.backend.model.enums.tType;
import com.tstu.util.CustomLogger;
import com.tstu.util.Logger;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IdentifierTable implements com.tstu.backend.INameTable {

    private Logger logger = new CustomLogger(LexicalAnalyzer.class.getName());

    private static Set<Identifier> identifiers;

    public IdentifierTable() {
        identifiers = new HashSet<>();
    }

    private void addIdentifier(String name, tCat category) {
        Identifier identifier = new Identifier(name, category, null);

        identifiers.add(identifier);
    }

    private void addIdentifier(String name, tCat category, tType type) {
        Identifier identifier = new Identifier(name, category, type);
        identifiers.add(identifier);
    }

    @Override
    public Identifier getIdentifier(String name) {
        return identifiers.stream()
                .filter(i -> i.getName().equals(name))
                .findAny().orElseThrow();
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
//        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
//        INameTable nameTable = new IdentifierTable();
//
//        nameTable.recognizeAllIdentifiers(lexicalAnalyzer.recognizeAllLexem("Var a,b,c :Logical\n"));
    }

    @Override
    public Set<Identifier> getIdentifiers() {
        return identifiers;
    }
}
