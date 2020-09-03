import com.tstu.controllers.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        final URL resourc =MainWindow.class.getResource("/com/tstu/controllers/MainWindow.fxml");
        final URL resource = getClass().getClassLoader().getResource("com/tstu/fxmll.fxml");
        final URL resource1 = getClass().getClassLoader().getResource("src/main/java/com/tstu/fxmll.fxml");
        final URL resource2 = getClass().getClassLoader().getResource("fxmll.fxml");
        final URL resource3 = getClass().getClassLoader().getResource("../MainWindow.fxml");


        Parent root = FXMLLoader.load(getClass().getResource("com/tstu/MainWindow.fxml"));
        primaryStage.setTitle("Транслятор");
        primaryStage.setScene(new Scene(root));
        runStage(primaryStage);
    }

    public static void runStage(Stage stage) throws IOException
    {
        primaryStage = stage;
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
