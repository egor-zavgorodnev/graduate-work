package ru.tver.tstu.backend.generator;

import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * Класс - структура для хранения методов и полей генерируемого класса
 */
public class ByteCodeBuilder {

     Set<MethodNode> methods = new HashSet<>();
     Set<FieldNode> fields = new HashSet<>();

    public ByteCodeBuilder() {
    }

    public void addField(FieldNode fieldNode) {
        fields.add(fieldNode);
    }

    public void addInstruction(MethodNode methodNode, AbstractInsnNode insnNode) {
        methodNode.instructions.add(insnNode);
        methods.add(methodNode);
    }

    public Set<MethodNode> getMethods() {
        return methods;
    }

    public Set<FieldNode> getFields() {
        return fields;
    }
}
