package com.tstu.controllers;

import com.tstu.App;
import com.tstu.backend.compilier.Compilier;
import com.tstu.backend.generator.CodeGenerator;
import com.tstu.execution.Executor;
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
    private Button ExecuteButton;

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
        boolean isCompiled = compilier.compile(sourceCodeBox.getText());
        if (isCompiled) {
            resultBox.setText(CodeGenerator.generateCode());
            ExecuteButton.setVisible(true);
            compileStatusBox.appendText("Программа успешно скомпилировалась \n");
        } else {
            compileStatusBox.appendText("Произошла ошибка компиляции \n");
            ExecuteButton.setVisible(false);
        }
        CodeGenerator.clear();
    }


    @FXML
    void openFileDialog(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        if (file != null) {
            sourceCodeBox.clear();
            resultBox.clear();
            compileStatusBox.clear();
            ExecuteButton.setVisible(false);
            ReadFromFile readFromFile = new ReadFromFile();
            String text = readFromFile.parseFromFile(file.getPath());
            sourceCodeBox.appendText(text);
        }
    }

    @FXML
    void execute(ActionEvent event) {
        try {
            Executor.execute();
        } catch (IOException | InterruptedException e) {
            appendLog(e.getMessage());
        }
    }

    public void appendLog(String log) {
        logsBox.appendText(log);
    }

}

