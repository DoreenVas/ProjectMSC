package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainWindow {
    @FXML
    private JFXButton submit;
    @FXML
    private JFXButton signUp;
    @FXML
    private JFXTextField id;
    @FXML
    private Button exit;


    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) this.exit.getScene().getWindow();
        stage.close();
    }

}
