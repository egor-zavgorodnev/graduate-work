package ru.tver.tstu.backend.model.enums;

public enum Command {
    VAR("Var"), CONST("Const"), PROCEDURE("Procedure"), CALL("Call"), BEGIN("Begin"), END("End"), IF("If"),
    THEN("Then"), WHILE("While"), DO("Do"), ODD("Odd");

    String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
