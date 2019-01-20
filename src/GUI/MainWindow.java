package GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

public class MainWindow {
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
            Stage stage = (Stage) instructions.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("InstructionsWindow.fxml"));
            stage.setTitle("Instructions");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width - 5,  this.window_height- 15);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setResizable(false);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Loading the Search window when the "start" button is clicked.
     */
    @FXML
    protected void start() {
        try {
            Stage stage = (Stage) start.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("GameWindow.fxml"));
            stage.setTitle("Game");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width - 5,  this.window_height- 15);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setResizable(false);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
