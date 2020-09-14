package com.tstu.backend.generator;

public class CodeGenerator {

    private static StringBuilder assemblerCode = new StringBuilder();

    public static void addInstruction(String instruction) {
        System.out.println(instruction);
        assemblerCode.append(instruction);
        assemblerCode.append('\n');
    }

    public static void generateFile() {

    }

    public static void declareDataSegment() {
        addInstruction("data segment para public \"data\"");
    }

    public static void declareStackAndCodeSegments() {
        addInstruction("PRINT_BUF DB ' ' DUP(10)");
        addInstruction("BUFEND DB '$'");
        addInstruction("data ends");
        addInstruction("stk segment stack");
        addInstruction("db 256 dup (\"?\")");
        addInstruction("stk ends");
        addInstruction("code segment para public \"code\"");
        addInstruction("main proc");
        addInstruction("assume cs:code,ds:data,ss:stk");
        addInstruction("mov ax,data");
        addInstruction("mov ds,ax");
    }

    public static void declarePrintProcedure() {
        addInstruction("PRINT PROC NEAR");
        addInstruction("MOV CX, 10");
        addInstruction("MOV DI, BUFEND - PRINT_BUF");
        addInstruction("PRINT_LOOP:");
        addInstruction("MOV DX, 0");
        addInstruction("DIV CX");
        addInstruction("ADD DL, '0'");
        addInstruction("MOV [PRINT_BUF + DI - 1], DL");
        addInstruction("DEC DI");
        addInstruction("CMP AL, 0");
        addInstruction("JNE PRINT_LOOP");
        addInstruction("LEA DX, PRINT_BUF");
        addInstruction("ADD DX, DI");
        addInstruction("MOV AH, 09H");
        addInstruction("INT 21H");
        addInstruction("RET");
        addInstruction("PRINT ENDP");
    }

    public static void declareEndMainProcedure() {
        addInstruction("mov ax,4c00h");
        addInstruction("int 21h");
        addInstruction("main endp");
    }

    public static void declareEndProgram() {
        addInstruction("code ends");
        addInstruction("end main");
    }


}
