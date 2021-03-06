package GUI;

import Model.Connection;
import Resources.AlertMessages;
import Resources.Alerter;
import Resources.PatientContainer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/******
 * In this window a new user can enter his/her details and submit into the system.
 * All information will be saved in the DB for further sign up.
 */
public class SignUpWindow extends BasicWindow implements Initializable {

    @FXML
    private ImageView home;
    @FXML
    private Button submit;
    @FXML
    private RadioButton radio_patient;
    @FXML
    private RadioButton radio_tester;
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

    /*******
     * By clicking the submit button a verification check of all fields
     * will be preformed.
     *
     * In case one of the fields is incorrect, an error will raise
     * and the information will not be sent.
     *
     * If all fields are correct, the information will be saved into the DB and
     * user will log in.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // allow enter press on start button
        this.submit.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                submit();
            }
        });
    }
    @FXML
    protected void submit() {
        try {
            String name = firstName.getText()+ " " + lastName.getText();
            String id = this.id.getText();
            String gender = (String)this.gender.getValue();
            String dominant_hand = (String)this.dominantHand.getValue();
            LocalDate date = this.date.getValue();
            String patient_type = "";
            if (radio_patient.isSelected())
                patient_type = "patient";
            else
                patient_type = "tester";
            //prepare correct msg
            String id_msg = "";
            String gender_msg = "";
            String date_msg = "";
            String hand_msg = "";
            switch (MainWindow.language) {
                case "Hebrew":
                    id_msg = "יש להזין 4 ספרות אחרונות של תעודת זהות";
                    gender_msg = "יש להזין את מין המשתמש/ת";
                    date_msg = "יש להזין תאריך לידה תקין";
                    hand_msg = "יש להזין יד דומיננטית";
//                    break;
                case "English":
                    id_msg = "please enter the last 4 digits of your ID";
                    gender_msg = "please enter gender";
                    date_msg = "please enter a valid birth date";
                    hand_msg = "please enter dominant hand";
                    break;
            }

            // validation checks
            if(id.length() != 4) {
                Alerter.showAlert(id_msg, Alert.AlertType.ERROR);
            } else if(gender == null) {
                Alerter.showAlert(gender_msg, Alert.AlertType.ERROR);
            } else if(date == null || date.compareTo(LocalDate.now()) > 0) {
                Alerter.showAlert(date_msg, Alert.AlertType.ERROR);
            }else if(dominant_hand == null) {
                Alerter.showAlert(hand_msg, Alert.AlertType.ERROR);
            } else {
                switch (gender) {
                    case "זכר":
                        gender = "M";
                        break;
                    case "male":
                        gender = "M";
                        break;
                    case "נקבה":
                         gender = "F";
                         break;
                    case "female":
                        gender = "F";
                        break;
                }

                switch (dominant_hand) {
                    case "ימין":
                        dominant_hand = "R";
                        break;
                    case "right":
                        dominant_hand = "R";
                        break;
                    case "שמאל":
                        dominant_hand = "L";
                        break;
                    case "left":
                        dominant_hand = "L";
                        break;
                }
                // inserting the new patient to the DB.
                Connection conn = Connection.getInstance();
                conn.insertPatientQuery(PatientContainer.getInstance().setPatientAge(date.toString())
                        .setPatientDominantHand(dominant_hand).setPatientGender(gender).setPatientID(id).setPatientName(name).setPatientType(patient_type));
                super.menuWindow(this.home);
            }
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /*****
     * By clicking the return button - we go back to the main window.
     */
    @FXML
    protected void mainWindow() {
        try {
            Stage stage = (Stage) this.home.getScene().getWindow();
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
}
