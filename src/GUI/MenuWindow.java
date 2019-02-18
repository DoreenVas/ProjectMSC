package GUI;
import Resources.AlertMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.awt.*;

public class MenuWindow {
    @FXML
    private Button start;
    @FXML
    private Button instructions;
    @FXML
    private Button exit;

    private double window_height;
    private double window_width;

    @FXML
    protected void instructions() {
        try {
            Stage stage = (Stage) this.instructions.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("InstructionsWindow.fxml"));
            stage.setTitle("Instructions");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width ,  this.window_height);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    protected void start() {
        try {
            Stage stage = (Stage) this.start.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("GameWindow.fxml"));
            stage.setTitle("Game");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width,  this.window_height);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) this.exit.getScene().getWindow();
        stage.close();
    }
}
