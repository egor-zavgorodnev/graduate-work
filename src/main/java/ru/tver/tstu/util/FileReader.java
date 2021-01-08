package ru.tver.tstu.util;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileReader {
    private static final Logger logger = Logger.getLogger(FileReader.class.getName());
            //= new CustomLogger(FileReader.class.getName());

    public static String parseFromFile(String filePath) {
        StringBuilder input = new StringBuilder();
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(input::append);
            logger.info("Чтение из файла");
        } catch (IOException e) {
            e.fillInStackTrace();
            logger.info("Неправильный путь к файлу");
        }
        return input.toString();
    }
}
