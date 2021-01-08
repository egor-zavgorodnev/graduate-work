package ru.tver.tstu.backend.generator.bytecode;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ArrayList;
import java.util.List;

public class BCG {

    static List<AbstractInsnNode> insnNodeList = new ArrayList<>();

    public static void addInstr(AbstractInsnNode node) {
        insnNodeList.add(node);
    }

    public static List<AbstractInsnNode> getInsnNodeList() {
        return insnNodeList;
    }
}
