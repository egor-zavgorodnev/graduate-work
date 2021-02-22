package ru.tver.tstu.execution;

public class ByteCodeLoader extends ClassLoader {
    public static ByteCodeLoader clazz = new ByteCodeLoader();

    public Class<?> loadClass(byte[] bytecode) {
        return defineClass(null, bytecode, 0, bytecode.length);
    }

    public static void clear() {
        clazz = new ByteCodeLoader();
    }
}
