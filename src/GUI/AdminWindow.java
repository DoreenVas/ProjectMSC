package GUI;

import Model.Connection;
import Resources.AlertMessages;
import Resources.Alerter;
import Resources.PatientContainer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminWindow implements Initializable{
    @FXML
    private JFXButton submit;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXButton exit;

    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // allow enter key press
        this.id.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                submit();
            }
        });
    }

    @FXML
    protected void submit() {
        try {
            // check validation of id number
            int id = Integer.parseInt(this.id.getText());
            if (id < 0 || id > 999999999) {
                throw new NumberFormatException();
            }
            // get the information of the patient
            String idString = this.id.getText();
            Connection conn = Connection.getInstance();
            PatientContainer p_info = conn.idQuery(idString);
            // patient doesn't exist in the database
            if (p_info == null) {
                Alerter.showAlert("תעודת זהות לא במערכת. נסה שנית.", Alert.AlertType.WARNING);
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("GraphsWindow.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage stage = (Stage) this.submit.getScene().getWindow();
                // get the size of the screen
                Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                this.window_height = window.height;
                this.window_width = window.width;
                // set the window size
                Scene scene = new Scene(root,  this.window_width,  this.window_height);
                GraphsWindow graphsWindow = loader.getController();
                graphsWindow.setPreviousScene("AdminWindow.fxml");
                stage.setTitle("Patient Data");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setMaximized(true);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alerter.showAlert("תעודת זהות לא תקינה! נסה שוב.", Alert.AlertType.WARNING);
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
