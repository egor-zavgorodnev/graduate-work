package com.tstu;

import com.tstu.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class MainWindow {

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
    void compile(ActionEvent event) {

    }

    @FXML
    void openFileDialog(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(App.getCurrentStage());
    }

}
