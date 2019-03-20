package GUI;

import Resources.AlertMessages;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

public class SettingsWindow extends BasicWindow {
    // members
    @FXML
    private Button home;
    @FXML
    private Button confirm;
    @FXML
    private JFXComboBox gameType;
    @FXML
    private JFXComboBox  timeLimit;
    @FXML
    private JFXComboBox  keyboard;

    private double window_height;
    private double window_width;

    @FXML
    protected void confirm() {
        try {
            Stage stage = (Stage) this.confirm.getScene().getWindow();
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

    @FXML
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    @FXML
    protected void logout() {
        super.logout();
    }
}
