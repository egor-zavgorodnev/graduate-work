package ru.tver.tstu.backend.generator.bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Set;

public class ByteCodeBuilder {

    static Set<MethodNode> methods = new HashSet<>();
    static Set<FieldNode> fields = new HashSet<>();

    public static void addField(FieldNode fieldNode) {
        fields.add(fieldNode);
    }

    public static void addInstruction(MethodNode methodNode, AbstractInsnNode insnNode) {
        methodNode.instructions.add(insnNode);
        methods.add(methodNode);
    }

    public static Set<MethodNode> getMethods() {
        return methods;
    }

    public static Set<FieldNode> getFields() {
        return fields;
    }
}