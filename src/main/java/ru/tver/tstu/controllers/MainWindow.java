package ru.tver.tstu.controllers;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import ru.tver.tstu.*;
import ru.tver.tstu.backend.compilier.*;
import ru.tver.tstu.execution.*;
import ru.tver.tstu.util.*;

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
    private Button compileButton;

    @FXML
    private TextArea logsBox;

    @FXML
    private Button clearLogs;

    @FXML
    private Button ExecuteButton;

    @FXML
    private MenuItem fileButton;

    @FXML
    private MenuItem shadowMode;

    @FXML
    void clear() {
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
        if (sourceCodeBox.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Отсутствует исходный код для cборки");
            alert.showAndWait();
            return;
        }
        boolean isCompiled = compilier.compile(sourceCodeBox.getText());
        if (isCompiled) {
            ExecuteButton.setVisible(true);
            compileStatusBox.appendText("Программа успешно скомпилировалась \n");
        } else {
            compileStatusBox.appendText("Произошла ошибка компиляции \n");
            ExecuteButton.setVisible(false);
        }
        //TODO clear
        //CodeGenerator.clear();
    }


    @FXML
    void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        if (file != null) {
            sourceCodeBox.clear();
            compileStatusBox.clear();
            ExecuteButton.setVisible(false);
            String text = FileReader.parseFromSourceCodeFile(file.getPath());
            sourceCodeBox.appendText(text);
        }
    }

    @FXML
    void execute() {
        try {
            Executor.execute(sourceCodeBox.getText());
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            appendLog(e.getMessage());
        }
    }

    @FXML
    void switchToShadowMode(ActionEvent event) {
        Menu menu = (Menu)event.getSource();
        CheckMenuItem checkItem = (CheckMenuItem) menu.getItems().get(0);
        if (checkItem.isSelected()) {
            sourceCodeBox.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-font-size: 18px; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
        }
        else {
            sourceCodeBox.setStyle("");
        }
    }

    @FXML
    void getInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Инфо");
        alert.setHeaderText("О программе");
        alert.setContentText("Версия: v1.0.0");
        alert.setContentText("Автор: Егор Завгороднев\nGithub: egorka99");
        alert.showAndWait();
    }

    public void appendLog(String log) {
        logsBox.appendText(log);
    }

}

