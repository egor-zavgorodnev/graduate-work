package com.tstu.backend.model;

import com.tstu.backend.model.enums.tCat;
import com.tstu.backend.model.enums.tType;

import java.util.Objects;

public class Identifier {
    private String name;
    private tCat category;
    private tType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public tCat getCategory() {
        return category;
    }

    public void setCategory(tCat category) {
        this.category = category;
    }

    public tType getType() {
        return type;
    }

    public void setType(tType type) {
        this.type = type;
    }

    public Identifier(String name, tCat category, tType type) {
        this.name = name;
        this.category = category;
        this.type = type;
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
