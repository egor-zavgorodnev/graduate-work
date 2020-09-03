package com.tstu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFromFile {
    private Logger logger = LoggerFactory.getLogger(ReadFromFile.class);

    public String parseFromFile(String filePath) {
        StringBuilder input = new StringBuilder();
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(input::append);
            logger.info("Чтение из файла");
        } catch (IOException e) {
            logger.info("Неправильный путь к файлу");
        }
        return input.toString();
    }

}
