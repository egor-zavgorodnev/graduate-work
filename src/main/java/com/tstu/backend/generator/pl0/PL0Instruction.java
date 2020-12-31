package com.tstu.backend.generator.pl0;

import com.tstu.backend.model.enums.Function;

public class PL0Instruction {

    private Function function;
    private int level;
    private String address;

    public PL0Instruction(Function function, int level, String address) {
        this.function = function;
        this.level = level;
        this.address = address;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
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
}
