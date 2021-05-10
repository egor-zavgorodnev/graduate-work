package ru.tver.tstu.backend.model;

import ru.tver.tstu.backend.model.enums.IdentifierCategory;

import java.util.Objects;

/**
 * Класс, представляющий идентификатор
 */
public class Identifier {
    private String name;
    private IdentifierCategory category;

    private int value;
    private int level;
    private String address;

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
