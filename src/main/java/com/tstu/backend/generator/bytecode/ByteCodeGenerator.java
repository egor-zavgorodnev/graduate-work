package com.tstu.backend.generator.bytecode;

import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ByteCodeGenerator {
    public static void main(String[] args) {
        ClassNode cn = new ClassNode();

        cn.version = V1_8; //версия библеотеки ASM
        cn.access = ACC_PUBLIC + ACC_SUPER; //класс публичный, что значит ACC_SUPER, могу лишь гадать
        cn.name = "ClassTest"; //имя класса
        cn.superName = "java/lang/Object"; //имя родительского класса

        //создаем метод public void <init>()
        MethodNode mn = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
        //список инструкий метода
        InsnList il = mn.instructions;

        //вызывем родительский конструктор
        //вполне видна взаимосвязь с байткодом JVM
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(new MethodInsnNode(INVOKESPECIAL, cn.superName, "<init>", "()V", false));

        //выход
        il.add(new InsnNode(RETURN));

        //добавляем метод к классу
        cn.methods.add(mn);
    }
}
