package com.tstu.backend.generator.bytecode;

import com.tstu.backend.generator.pl0.PL0Instruction;
import com.tstu.backend.model.enums.OpCode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeGenerator {

    private static final String CLASS_FILE_PATH = "file.class"; //root dir

    private Map<Integer, PL0Instruction> pl0Instructions;
    private ClassNode classNode;
    List<Integer> instructionsNeedLabel = new ArrayList<>();

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

        classNode.accept(cw);

        return cw.toByteArray();
    }

    private void generateWrapperClassAndInitMethod() {

        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC + ACC_SUPER;
        classNode.name = "ClassTest";
        classNode.superName = "java/lang/Object";
        classNode.interfaces.add("java/lang/Runnable");

        MethodNode initMethodNode = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);

        InsnList initMethodInstructionList = initMethodNode.instructions;

        initMethodInstructionList.add(new VarInsnNode(ALOAD, 0));
        initMethodInstructionList.add(new MethodInsnNode(INVOKESPECIAL, classNode.superName, "<init>", "()V", false));

        initMethodInstructionList.add(new InsnNode(RETURN));

        classNode.methods.add(initMethodNode);

    }

    private void generateRunMethod() {

        MethodNode runMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);

        InsnList runMethodInstructionList = runMethodNode.instructions;

        for (AbstractInsnNode node : BCG.getInsnNodeList()) {
            runMethodInstructionList.add(node);
        }

        classNode.methods.add(runMethodNode);
    }

    private LabelNode getLabelForInstruction(int number) {
        instructionsNeedLabel.add(number);
        return new LabelNode();
    }

    private void writeFile() {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classNode.accept(cw);

        try (FileOutputStream fos = new FileOutputStream(CLASS_FILE_PATH)) {
            fos.write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
