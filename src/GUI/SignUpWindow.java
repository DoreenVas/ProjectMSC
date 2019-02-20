package GUI;

import Resources.AlertMessages;
import Resources.PatientContainer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.time.LocalDate;

public class SignUpWindow extends BasicWindow{

    @FXML
    private Button home;
    @FXML
    private Button submit;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXComboBox gender;
    @FXML
    private JFXComboBox dominantHand;
    @FXML
    private DatePicker date;

    private double window_height;
    private double window_width;

    @FXML
    protected void submit() {
        try {
            String name = firstName.getText()+ " " + lastName.getText();
            String id = this.id.getText();
            String gender = (String)this.gender.getValue();
            String dominant_hand = (String)this.dominantHand.getValue();
            LocalDate date = this.date.getValue();
            // validation checks
            if(id.length() != 9) {
                Alerter.showAlert("יש להזין תעודת זהות תקינה בעלת 9 ספרות", Alert.AlertType.ERROR);
            } else if(gender == null) {
                Alerter.showAlert("יש להזין את מין המשתמש/ת", Alert.AlertType.ERROR);
            } else if(dominant_hand == null) {
                Alerter.showAlert("ש להזין יד דומיננטית", Alert.AlertType.ERROR);
            } else if(date == null || date.compareTo(LocalDate.now()) > 0) {
                Alerter.showAlert("יש להזין תאריך לידה תקין", Alert.AlertType.ERROR);
            } else {
                switch (gender) {
                    case "זכר":
                        gender = "M";
                        break;
                    default:
                         gender = "F";
                         break;
                }

                switch (dominant_hand) {
                    case "ימין":
                        dominant_hand = "R";
                        break;
                    default:
                        dominant_hand = "L";
                        break;
                }
                Connection conn = Connection.getInstance();
                conn.insertPatientQuery(PatientContainer.getInstance().setPatientAge(date.toString())
                        .setPatientDominantHand(dominant_hand).setPatientGender(gender).setPatientID(id).setPatientName(name));
                //TODO fix here
                super.menuWindow();
            }
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void mainWindow() {
        try {
            Stage stage = (Stage) this.home.getScene().getWindow();
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
}
