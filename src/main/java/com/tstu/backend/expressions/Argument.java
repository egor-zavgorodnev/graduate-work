package com.tstu.backend.expressions;

import com.tstu.backend.model.Identifier;

public class Argument<T> {
    private Identifier variable;
    private T value;

    public Argument(Identifier variable, T value) {
        this.variable = variable;
        this.value = value;
    }

    public Identifier getVariable() {
        return variable;
    }

    public T getValue() {
        return value;
    }
}
