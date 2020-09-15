package com.tstu.backend.generator;

import com.tstu.util.CustomLogger;
import com.tstu.util.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class CodeGenerator {

    private static Logger logger = new CustomLogger(CodeGenerator.class.getName());

    private static StringBuilder assemblerCode = new StringBuilder();

    private static Properties appProps = new Properties();

    public static void addInstruction(String instruction) {
        assemblerCode.append(instruction);
        assemblerCode.append('\n');
    }

    public static void generateFile() throws IOException {
        logger.info("\n---Генерация кода---\n");
        logger.info("Создание .asm файла");
        appProps.load(Objects.requireNonNull(CodeGenerator.class.getClassLoader().getResourceAsStream("application.properties")));
        File asmCode = new File(appProps.getProperty("asmEnviroment") + "/sample.asm");
        if (asmCode.exists()) asmCode.delete();
        FileWriter myWriter = new FileWriter(asmCode);
        myWriter.write(assemblerCode.toString());
        myWriter.close();
        logger.info(".asm файл создан");
        logger.info("Программа скомпилирована!");
    }

    public static String generateCode() {
        return assemblerCode.toString();
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
