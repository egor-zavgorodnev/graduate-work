package ru.tstu.tver.backend.lexems;

import ru.tstu.tver.backend.INameTable;
import ru.tstu.tver.backend.exceptions.LexicalAnalyzeException;
import ru.tstu.tver.backend.model.Identifier;
import ru.tstu.tver.backend.model.enums.Lexem;
import ru.tstu.tver.backend.model.Keyword;
import ru.tstu.tver.backend.model.enums.Command;
import ru.tstu.tver.backend.model.enums.IdentifierCategory;
import org.apache.log4j.Logger;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IdentifierTable implements INameTable {

    private Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());

    private static Set<Identifier> identifiers;

    public IdentifierTable() {
        identifiers = new HashSet<>();
    }

    private void addIdentifier(String name, IdentifierCategory category) {
        Identifier identifier = new Identifier(name, category);

        identifiers.add(identifier);
    }

    @Override
    public Identifier getIdentifier(String name) throws LexicalAnalyzeException {
        return identifiers.stream()
                .filter(i -> i.getName().equals(name))
                .findAny().orElse(null);
    }

    @Override
    public void recognizeAllIdentifiers(List<Keyword> keywords) {
        logger.info("\n---Разбор идентификаторов---\n");
        for (Keyword keyword : keywords) {
            if (keyword.lex.equals(Lexem.NAME)) {
                if (EnumSet.allOf(Command.class).stream().anyMatch(e -> e.getName().equals(keyword.word))) {
                    addIdentifier(keyword.word, IdentifierCategory.COMMAND);
                    logger.info(keyword.word + "(команда)");
                } else {
                    addIdentifier(keyword.word, IdentifierCategory.NONE);
                    logger.info(keyword.word + "(нет данных)");
                }
            }
        }

    }

    @Override
    public Set<Identifier> getIdentifiers() {
        return identifiers;
    }

    @Override
    public void clear() {
        identifiers.clear();
    }
}
