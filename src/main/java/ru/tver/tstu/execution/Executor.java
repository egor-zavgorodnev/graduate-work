package ru.tver.tstu.execution;

import java.io.*;

/**
 * Класс, отвечающий за исполнение исходного кода программы
 */
public class Executor {

    public static void execute(String sourceCode) throws IOException {

        File startBat = new File("start.bat");
        FileWriter fileWriter = new FileWriter(startBat);

        fileWriter.write("@echo off\n");
        fileWriter.write("java -jar executor.jar \"" + sourceCode.replaceAll("\n", "/n") + "\"");
        fileWriter.write("\npause");
        fileWriter.write("\nexit");
        fileWriter.close();

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(new String[]{"cmd.exe","/c","start start.bat"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
