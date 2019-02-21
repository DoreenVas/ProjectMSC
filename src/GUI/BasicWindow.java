package GUI;

import Resources.AlertMessages;
import Resources.PatientContainer;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BasicWindow implements Initializable{
    @FXML
    private Button back = new Button();
    @FXML
    private Button exit = new Button();
    @FXML
    private Hyperlink logoutLabel = new Hyperlink();
    @FXML
    private Label patientName = new Label();


    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.patientName.textProperty().bind(Bindings.convert(PatientContainer.getInstance().valueProperty()));
    }

    /****
     * Returns to the Main window. Used by all windows.
     */
    @FXML
    protected void logout() {
        this.logoutLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //copy over the button's event.
                PatientContainer.getInstance().logOut();
            }
        });
        try {
            Stage stage = (Stage) this.logoutLabel.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
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
    protected void menuWindow(Button button) {
        try {
            Stage stage = (Stage) button.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("MenuWindow.fxml"));
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

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) this.exit.getScene().getWindow();
        stage.close();
    }
}