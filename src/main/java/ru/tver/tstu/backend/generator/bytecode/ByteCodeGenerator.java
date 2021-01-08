package ru.tstu.tver.backend.generator.bytecode;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeGenerator {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(ByteCodeGenerator.class.getName());
    private static final String CLASS_FILE_PATH = "file.class"; //root dir

    private static final ClassNode classNode = new ClassNode();

    public static void generateAsFileByPath() {
        generateWrapperClassAndInitMethod();
        generateRunMethod();
        writeFile();
    }

    public static byte[] generateAsByteArray() {
        generateWrapperClassAndInitMethod();
        generateRunMethod();
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classNode.accept(cw);

        return cw.toByteArray();
    }

    private static void generateWrapperClassAndInitMethod() {

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

    private static void generateRunMethod() {

        MethodNode runMethodNode = new MethodNode(ACC_PUBLIC, "run", "()V", null, null);

        InsnList runMethodInstructionList = runMethodNode.instructions;

        for (AbstractInsnNode node : BCG.getInsnNodeList()) {
            runMethodInstructionList.add(node);
        }

        classNode.methods.add(runMethodNode);
    }


    private static void writeFile() {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classNode.accept(cw);

        try (FileOutputStream fos = new FileOutputStream(CLASS_FILE_PATH)) {
            fos.write(cw.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
