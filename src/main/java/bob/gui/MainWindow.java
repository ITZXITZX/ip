package bob.gui;

import bob.Bob;
import bob.ui.Ui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Bob bob;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    /**
     * Initialises the chatBot.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String welcomeMessage = Ui.getWelcomeMessage();
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(welcomeMessage, dukeImage, "welcome")
        );
    }

    /** Injects the Duke instance */
    public void setBob(Bob d) {
        bob = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        terminateIfBye(input);
        String response = bob.getResponse(input);
        String commandType = bob.getCommandType();
        assert response != null : "Response from execution not null";
        assert commandType != null : "Command Type not null.";
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage, commandType)
        );
        userInput.clear();
    }

    @FXML
    private void terminateIfBye(String input) {
        if (input.equals("bye")) {
            Platform.exit();
        }
    }
}

