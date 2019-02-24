package GUI;

import Resources.AlertMessages;
import Resources.PatientContainer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuWindow extends BasicWindow implements Initializable{
    @FXML
    private Button start;
    @FXML
    private Button instructions;
    @FXML
    private Button settings;

    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.start.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                start();
            }
        });

        this.instructions.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                instructions();
            }
        });

        this.settings.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                settings();
            }
        });
    }

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

    @FXML
    protected void settings() {
        try {
            Stage stage = (Stage) this.settings.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("SettingsWindow.fxml"));
            stage.setTitle("Settings");
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
    protected void logout() {
        super.logout();
    }

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        super.exit();
    }
}
