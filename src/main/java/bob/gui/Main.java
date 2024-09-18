package bob.gui;

import java.io.IOException;

import bob.Bob;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class for the gui.
 */
public class Main extends Application {

    private static final String DEFAULT_FILE_PATH = "src/main/java/bob/data/tasks.txt";

    private Bob bob = new Bob(DEFAULT_FILE_PATH);

    // Existing constructor
    public Main(String filePath) {
        // ...
    }

    // Overloaded constructor
    public Main() {
        this(DEFAULT_FILE_PATH);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setBob(bob); // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

