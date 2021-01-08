package ru.tver.tstu.backend.generator.pl0;

import ru.tver.tstu.backend.model.enums.OpCode;

public class PL0Instruction {

    private OpCode opCode;
    private int level;
    private String address;

    public PL0Instruction(OpCode opCode, int level, String address) {
        this.opCode = opCode;
        this.level = level;
        this.address = address;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public void setFunction(OpCode opCode) {
        this.opCode = opCode;
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
