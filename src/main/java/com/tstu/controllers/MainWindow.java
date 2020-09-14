package com.tstu.controllers;

import com.tstu.App;
import com.tstu.backend.compilier.Compilier;
import com.tstu.util.ReadFromFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainWindow {

    private final Path watchPath = Paths.get("logging.txt");

    @FXML
    private TextArea sourceCodeBox;

    @FXML
    private TextArea resultBox;

    @FXML
    private TextArea compileStatusBox;

    @FXML
    private MenuItem fileButton;

    @FXML
    private MenuItem compileButton;

    @FXML
    private TextArea logsBox;

    @FXML
    private Button clearLogs;

    @FXML
    void clear(ActionEvent event) {
        logsBox.clear();
        try {
            Files.newBufferedWriter(watchPath, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void compile(ActionEvent event) {
        Compilier compilier = new Compilier();
        compilier.compile(sourceCodeBox.getText());
    }


    @FXML
    void openFileDialog(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());

        ReadFromFile readFromFile = new ReadFromFile();
        String text = readFromFile.parseFromFile(file.getPath());
        sourceCodeBox.appendText(text);
    }

    public void appendLog(String log) {
        logsBox.appendText(log);
    }

}

