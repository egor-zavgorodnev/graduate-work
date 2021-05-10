package ru.tver.tstu.execution;

/**
 * Класс, загружающий создающий класс из байт кода
 */
public class ByteCodeLoader extends ClassLoader {
    public static ByteCodeLoader clazz = new ByteCodeLoader();

    public Class<?> loadClass(byte[] bytecode) {
        return defineClass(null, bytecode, 0, bytecode.length);
    }

    public static void clear() {
        clazz = new ByteCodeLoader();
    }
}
