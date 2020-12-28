package com.tstu.backend.generator;

import com.tstu.backend.model.enums.Function;
import org.apache.log4j.Logger;

public class PL0CodeGenerator {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(PL0CodeGenerator.class.getName());

    private static StringBuilder stackMachineCode = new StringBuilder();

    private static int instructionNumber;

    public static void addInstruction(Function function, int level, String address) {
        logger.info(instructionNumber++ + " " + function + " " + level + " " + address);
        stackMachineCode.append('\n');
    }

 


}
