package com.tstu.backend.expressions;

public class Operation {
    private String sign;
    private int priority;

    public Operation(String sign, int priority) {
        this.sign = sign;
        this.priority = priority;
    }

    public String getSign() {
        return sign;
    }

    public int getPriority() {
        return priority;
    }
}
