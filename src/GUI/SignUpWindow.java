package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpWindow implements Initializable{

    @FXML
    private Button back;

    private double window_height;
    private double window_width;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private ComboBox<String> dominantHand;

    private ObservableList<String> genderSet = FXCollections.observableArrayList("נקבה","זכר");
    private ObservableList<String> handSet = FXCollections.observableArrayList("שמאל","ימין");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gender.setItems(genderSet);
        dominantHand.setItems(handSet);
    }

    @FXML
    protected void mainWindow() {
        try {
            Stage stage = (Stage) this.back.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            stage.setTitle("MSC");
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
            e.printStackTrace();
        }
    }
}
