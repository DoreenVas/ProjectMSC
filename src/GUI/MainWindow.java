package GUI;

import Model.Connection;
import Resources.AlertMessages;
import Resources.Alerter;
import Resources.PatientContainer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow implements Initializable{
    public static String language = "Hebrew";

    @FXML
    private JFXButton submit;
    @FXML
    private JFXButton signUp;
    @FXML
    private JFXButton admin_submit;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField admin_user;
    @FXML
    private JFXPasswordField admin_password;
    @FXML
    private Button exit;
    @FXML
    private JFXToggleButton change_lng;

    private String user, password;
    private double window_height;
    private double window_width;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // allow enter press on submit button
        this.id.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                submit();
            }
        });

        // allow enter press on admin button
        this.admin_submit.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                admin_submit();
            }
        });
        // allow mouse key click
        this.admin_submit.setOnMouseClicked(event -> {
            admin_submit();
        });

        // allow enter press on admin button
        this.admin_password.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                admin_submit();
            }
        });

        // allow enter press on admin button
        this.admin_user.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                admin_submit();
            }
        });
    }

    @FXML
    protected void signUp() {
        try {
            Stage stage = (Stage) this.signUp.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource(language+"/SignUpWindow.fxml"));
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
                Alerter.showAlert("תעודת זהות לא במערכת. נסה שנית או הירשם.", Alert.AlertType.WARNING);
            } else {
                Stage stage = (Stage) this.submit.getScene().getWindow();
                AnchorPane root = FXMLLoader.load(getClass().getResource(language+"/MenuWindow.fxml"));
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
            }
        } catch (NumberFormatException e) {
            Alerter.showAlert("תעודת זהות לא תקינה! נסה שוב.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void admin_submit() {
        try {
            // get the entered information
            String admin_user = this.admin_user.getText();
            String admin_password = this.admin_password.getText();
            // get the correct information from file
            parseInfo();
            // check validation of user and password
            if (!admin_user.equals(this.user) || !admin_password.equals(this.password) ) { // wrong details
                Alerter.showAlert("הפרטים שהוזנו אינם נכונים.", Alert.AlertType.WARNING);
            } else { //forward to admin page
                Stage stage = (Stage) this.admin_submit.getScene().getWindow();
                AnchorPane root = FXMLLoader.load(getClass().getResource(language+"/AdminWindow.fxml"));
                stage.setTitle("Admin");
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
            }
        }catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /*****
     * Parse the information of the admin_details file
     */
    private void parseInfo() {
        String row;
        String[] info;
        // Current working directory is ProjectMSC
        // the path to the config file
        String filePath = "src/Resources/admin_details";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            // read the info from the config file
            row = reader.readLine();
            while(row != null) {

                info = row.replace(" ","").split("=");
                switch(info[0]) {
                    case "user":
                        this.user = info[1];
                        break;
                    case "password":
                        this.password = info[1];
                        break;
                }
                row = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open config file reader\n");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read from config file\n");
            e.printStackTrace();
        }
    }

    @FXML
    protected void change_lng() {
        if(change_lng.isSelected()== true)
            language = "English";
        else
            language = "Hebrew";
        try{
            Stage stage = (Stage) this.change_lng.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource(language+"/MainWindow.fxml"));
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
        }catch (Exception e) {
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
