package com.tstu.execution;

import com.tstu.backend.generator.CodeGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Executor {

    private static Properties appProps = new Properties();

    public static void execute() throws IOException, InterruptedException {
        appProps.load(Objects.requireNonNull(CodeGenerator.class.getClassLoader().getResourceAsStream("application.properties")));

        File startBat = new File("start.bat");
        FileWriter fileWriter = new FileWriter(startBat);

        fileWriter.write("start DOSBox-0.74-3/DOSBox.exe -noconsole -conf \"DOSBox-0.74-3/asmProg.conf\"");
        fileWriter.close();

        String path="start.bat";
        Runtime rn=Runtime.getRuntime();
        Process pr=rn.exec(path);


    }
}
