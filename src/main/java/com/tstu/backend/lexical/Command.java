package com.tstu.backend.lexical;

public enum Command {
    VAR("Var"), BEGIN("Begin"), END("End"), PRINT("Print");

    String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
