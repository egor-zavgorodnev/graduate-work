package com.tstu.backend.generator.pl0;

import com.tstu.backend.model.enums.OpCode;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PL0CodeGenerator {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(PL0CodeGenerator.class.getName());

    //private static final StringBuilder stackMachineCode = new StringBuilder();

    private static int instructionNumber;

    private static Map<Integer, PL0Instruction> instructions = new HashMap();

    public static int addInstruction(OpCode opCode, int level, String address) {
        instructions.put(++instructionNumber, new PL0Instruction(opCode,level,address));
       // stackMachineCode.append('\n');
        return instructionNumber;
    }

    public static void printInstructions() {
        for (Map.Entry<Integer, PL0Instruction> entry : instructions.entrySet()) {
            logger.info(entry.getKey() + " " + entry.getValue().getOpCode() + " " + entry.getValue().getLevel()
                    + " " + entry.getValue().getAddress());
        }
    }

    // for fixup realize
    public static void changeInstructionAddress(int instructionNumber, int newAddress) {
        PL0Instruction PL0InstructionForChanging = instructions.get(instructionNumber);
        PL0InstructionForChanging.setAddress(String.valueOf(newAddress));
    }

    public static int getLastCodeAddress() {
        return instructionNumber;
    }

    public static Map<Integer, PL0Instruction> getInstructions() {
        return instructions;
    }
}
