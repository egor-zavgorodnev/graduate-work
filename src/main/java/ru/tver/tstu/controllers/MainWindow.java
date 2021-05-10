package ru.tver.tstu.controllers;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import ru.tver.tstu.*;
import ru.tver.tstu.backend.compilier.*;
import ru.tver.tstu.execution.*;
import ru.tver.tstu.util.FileReader;

import java.io.*;
import java.nio.file.*;

/**
 * JavaFx контроллер, обрабатывающий действия пользователя на UI
 */
public class MainWindow {

    private final Path watchPath = Paths.get("logging.txt");

    public static boolean errorOnly;

    @FXML
    private TextArea sourceCodeBox;

    @FXML
    private TextArea compileStatusBox;

    @FXML
    private Button compileButton;

    @FXML
    private TextArea logsBox;

    @FXML
    private Button ExecuteButton;

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
        logsBox.clear();
        compileStatusBox.clear();
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
        } catch (IOException e) {
            appendLog(e.getMessage());
        }
    }

    @FXML
    void switchToShadowMode(ActionEvent event) {
        CheckMenuItem checkItem = (CheckMenuItem) event.getSource();

        if (checkItem.isSelected()) {
            sourceCodeBox.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-font-size: 18px; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
        } else {
            sourceCodeBox.setStyle("");
        }
    }

    @FXML
    void showErrorsOnly(ActionEvent event) {
        CheckMenuItem checkItem = (CheckMenuItem) event.getSource();
        errorOnly = checkItem.isSelected();
    }

    @FXML
    void getInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Image image = new Image("forward.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        alert.setGraphic(imageView);
        alert.setTitle("Инфо");
        alert.setHeaderText("Транслятор");
        alert.setContentText("Версия: v1.0.0\nАвтор: Егор Завгороднев\nGithub: egorka99");
        alert.showAndWait();
    }

    public void appendLog(String log) {
        logsBox.appendText(log);
    }

}

