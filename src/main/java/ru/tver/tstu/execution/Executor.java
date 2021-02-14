package ru.tver.tstu.execution;

import java.io.*;


public class Executor {

    //root dir;

    public static void execute(String sourceCode) throws IllegalAccessException, InstantiationException, IOException {

        File startBat = new File("start.bat");
        FileWriter fileWriter = new FileWriter(startBat);

        fileWriter.write("@echo off\n");
        fileWriter.write("java -jar executor.jar " + sourceCode.replaceAll("\n", "/n"));
        fileWriter.write("\npause");
        fileWriter.close();

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(new String[]{"cmd.exe","/c","start start.bat"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
