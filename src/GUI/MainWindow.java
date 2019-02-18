package GUI;

import Resources.AlertMessages;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

public class MainWindow {
    @FXML
    private JFXButton submit;
    @FXML
    private JFXButton signUp;
    @FXML
    private JFXTextField id;
    @FXML
    private Button exit;

    private double window_height;
    private double window_width;


    @FXML
    protected void signUp() {
        try {
            Stage stage = (Stage) this.signUp.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("SignUpWindow.fxml"));
            stage.setTitle("MSC");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    protected void submit() {
        try {
            Stage stage = (Stage) this.submit.getScene().getWindow();
        } catch (Exception e) {
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
