package ru.tver.tstu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.stage.Stage;
import ru.tver.tstu.controllers.*;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static Stage primaryStage;
    private static MainWindow mainWindow;

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("fxml/MainWindow.fxml").openStream());
        mainWindow = fxmlLoader.getController();
        primaryStage.setTitle("Транслятор");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("file:forward.png"));
        runStage(primaryStage);
    }

    public static void runStage(Stage stage) {
        primaryStage = stage;
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
