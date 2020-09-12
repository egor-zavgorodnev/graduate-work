package com.tstu.controllers;

import com.tstu.App;
import com.tstu.backend.ILexicalAnalyzer;
import com.tstu.backend.exceptions.LexicalAnalyzeException;
import com.tstu.backend.lexems.LexicalAnalyzer;
import com.tstu.util.ReadFromFile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.*;

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
        ILexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        try {
            lexicalAnalyzer.recognizeAllLexem("var a,b,c 0 1 :Logical\n");
        } catch (LexicalAnalyzeException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // TODO Auto-generated method stub
        loadFile();
        FileAlterationObserver observer = new FileAlterationObserver("logging.txt");
        FileAlterationMonitor monitor = new FileAlterationMonitor(2);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                // code for processing creation event
            }

            @Override
            public void onFileDelete(File file) {
                // code for processing deletion event
            }

            @Override
            public void onFileChange(File file) {
                // code for processing change event
               // clear(new ActionEvent());
                loadFile();
            }
        };
        observer.addListener(listener);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


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


    private void loadFile() {
        try {
            String stringFromFile = Files.lines(watchPath).collect(Collectors.joining("\n"));
            logsBox.setText(stringFromFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

