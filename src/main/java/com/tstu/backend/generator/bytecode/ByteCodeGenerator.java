package com.tstu.backend.generator.bytecode;

import com.tstu.backend.generator.pl0.PL0Instruction;
import com.tstu.backend.model.enums.OpCode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeGenerator {

    private static final String CLASS_FILE_PATH = "file.class"; //root dir

    private Map<Integer, PL0Instruction> pl0Instructions;
    private ClassNode classNode;

    public ByteCodeGenerator(Map<Integer, PL0Instruction> pl0Instructions) {
        this.pl0Instructions = pl0Instructions;
        classNode = new ClassNode();
    }

    public void generateAsFileByPath() {
        generateWrapperClassAndInitMethod();
        generateRunMethod();
        writeFile();
    }

    public byte[] generateAsByteArray() {
        generateWrapperClassAndInitMethod();
        generateRunMethod();
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //связываем ClassNode с ClassWriter
        classNode.accept(cw);

        return cw.toByteArray();
    }

    private void generateWrapperClassAndInitMethod() {

        classNode.version = V1_8; //версия библеотеки ASM
        classNode.access = ACC_PUBLIC + ACC_SUPER; //класс публичный, что значит ACC_SUPER, могу лишь гадать
        classNode.name = "ClassTest"; //имя класса
        classNode.superName = "java/lang/Object"; //имя родительского класса
        classNode.interfaces.add("java/lang/Runnable"); //добавляем интерфейс

        //создаем метод public void <init>()
        MethodNode initMethodNode = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        //список инструкций метода
        InsnList initMethodInstructionList = initMethodNode.instructions;

        //вызывем родительский конструктор
        //вполне видна взаимосвязь с байткодом JVM
        initMethodInstructionList.add(new VarInsnNode(ALOAD, 0));
        initMethodInstructionList.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));

        //выход
        initMethodInstructionList.add(new InsnNode(RETURN));

        //добавляем метод к классу
        classNode.methods.add(initMethodNode);

    }

    private void generateRunMethod() {
        //создаем метод public void run()
        MethodNode runMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);
        //список инструкий метода
        InsnList runMethodInstructionList = runMethodNode.instructions;

        for (Map.Entry<Integer, PL0Instruction> entry : pl0Instructions.entrySet()) {
            OpCode opCode = entry.getValue().getOpCode();
            String address = entry.getValue().getAddress();
            switch (opCode) {
                case INT:
                    break;
                case LIT:
                    runMethodInstructionList.add(new LdcInsnNode(Integer.parseInt(address)));
                    break;
                case LOD:
                    runMethodInstructionList.add(new VarInsnNode(ILOAD, Integer.parseInt(address)));
                    break;
                case STO:
                    runMethodInstructionList.add(new VarInsnNode(ISTORE, Integer.parseInt(address)));
                    // print current variable value
                    runMethodInstructionList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                    runMethodInstructionList.add(new VarInsnNode(ILOAD, Integer.parseInt(address)));
                    runMethodInstructionList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false));
                    break;
                case OPR:
                    switch (address) {
                        case "+":
                            runMethodInstructionList.add(new InsnNode(IADD));
                            break;
                        case "-":
                            runMethodInstructionList.add(new InsnNode(ISUB));
                            break;
                        case "*":
                            runMethodInstructionList.add(new InsnNode(IMUL));
                            break;
                        case "/":
                            runMethodInstructionList.add(new InsnNode(IDIV));
                            break;
                        case "return":
                            runMethodInstructionList.add(new InsnNode(RETURN));
                            break;
                    }
                    break;
            }
        }

        //добавляем метод к классу
        classNode.methods.add(runMethodNode);
    }

    private void writeFile() {

        //класс, который позволяет получить байткод
        // параметр COMPUTE_FRAMES включает автоматический рассчет
        // кол-во используемых локальных переменных и максимальный размер стэка (эта информация нужна для JVM)
        // а так же рассчитывает переходы по меткам
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //связываем ClassNode с ClassWriter
        classNode.accept(cw);

        try (FileOutputStream fos = new FileOutputStream(CLASS_FILE_PATH)) {
            fos.write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
