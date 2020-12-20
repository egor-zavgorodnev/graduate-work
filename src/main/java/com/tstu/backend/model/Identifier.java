package com.tstu.backend.model;

import com.tstu.backend.model.enums.IdentifierCategory;

import java.util.Objects;

public class Identifier {
    private String name;
    private IdentifierCategory category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentifierCategory getCategory() {
        return category;
    }

    public void setCategory(IdentifierCategory category) {
        this.category = category;
    }

    public Identifier(String name, IdentifierCategory category) {
        this.name = name;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
