package ru.tver.tstu.backend.generator.bytecode;

import org.objectweb.asm.tree.*;

import java.util.*;

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
