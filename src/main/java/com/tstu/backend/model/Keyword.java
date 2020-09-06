package com.tstu.backend.model;

import com.tstu.backend.model.enums.Lexems;

import java.util.Objects;

public class Keyword {
    public String word;
    public Lexems lex;

    public Keyword(String word, Lexems lex) {
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
