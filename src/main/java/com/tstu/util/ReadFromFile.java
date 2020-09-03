package com.tstu.util;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class ReadFromFile {
    private Logger logger = Logger.getLogger(ReadFromFile.class.getName());

    public String parseFromFile(String filePath) {
        StringBuilder input = new StringBuilder();
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(e->input.append(e).append("\n"));
            logger.info("Чтение из файла");
        } catch (IOException e) {
            e.fillInStackTrace();
            logger.info("Неправильный путь к файлу");
        }
        return input.toString();
    }

}
