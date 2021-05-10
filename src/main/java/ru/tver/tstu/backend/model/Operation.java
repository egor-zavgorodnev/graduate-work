package ru.tver.tstu.backend.model;

/**
 * Класс, представляющий операцию
 */
public class Operation {
    private Keyword sign;
    private int priority;

    public Operation(Keyword sign, int priority) {
        this.sign = sign;
        this.priority = priority;
    }

    public Operation() {
    }

    public Keyword getSign() {
        return sign;
    }

    public int getPriority() {
        return priority;
    }
}
