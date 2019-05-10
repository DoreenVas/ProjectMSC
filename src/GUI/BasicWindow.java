package GUI;

import Resources.AlertMessages;
import Resources.Alerter;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BasicWindow implements Initializable{
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
        // show the patient name
        this.patientName.textProperty().bind(Bindings.convert(PatientContainer.getInstance().valueProperty()));

        // allow enter key press
        this.exit.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                exit();
            }
        });

        // allow mouse key click
        this.exit.setOnMouseClicked(event -> {
            exit();
        });
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
            AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindow.language+"/MainWindow.fxml"));
            stage.setTitle("MSC");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
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
            AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindow.language+"/MenuWindow.fxml"));
            stage.setTitle("MSC");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
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
