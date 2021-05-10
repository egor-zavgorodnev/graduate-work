package ru.tver.tstu.backend.model;

import ru.tver.tstu.backend.model.enums.Lexem;

import java.util.Objects;

/**
 * Класс, представляющий токен
 */
public class Keyword {
    public String word;
    public Lexem lex;

    public Keyword(String word, Lexem lex) {
        this.word = word;
        this.lex = lex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return word.equals(keyword.word) &&
                lex == keyword.lex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, lex);
    }
}
