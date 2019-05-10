package GUI;

import Resources.AlertMessages;
import Resources.Alerter;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

/******
 * This window determines the settings of the game that will be played.
 * The patient can choose and change the followings:
 * - the game type (shapes/textures/both).
 * - the time limit of each round in the game (30/45/60 seconds).
 * - the dominant hand - determine if he/she is going to use the dominant hand during the game.
 * - (not in current version) the keyboard number - for multi keyboard options.
 */
public class SettingsWindow extends BasicWindow {
    // members
    @FXML
    private ImageView home;
    @FXML
    private Button confirm;
    @FXML
    private JFXComboBox gameType;
    @FXML
    private JFXComboBox  timeLimit;
    @FXML
    private JFXComboBox  dominantHand;

    private double window_height;
    private double window_width;

    private String c_gameType;

    @FXML
    private void handle(KeyEvent event) {
    // allow enter press on submit button
        if (event.getCode() == KeyCode.ENTER) {
            this.confirm();
        }
    }

    /*****
     * clicking the confirm button will save the values and  start the game
     * according to those parameters.
     */
    @FXML
    protected void confirm() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MainWindow.language+"/GameWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.confirm.getScene().getWindow();
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width,  this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
            GameWindow gameWindow = loader.getController();
            convertData();
            gameWindow.initialize(c_gameType,(String)timeLimit.getValue(),(String)dominantHand.getValue());
            stage.setTitle("Game");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /******
     * converts the game type to english
     */
    private void convertData(){
        switch((String)gameType.getValue()){
            case("צורות"):
                this.c_gameType="Shapes";
                break;
            case("shapes"):
                this.c_gameType="Shapes";
                break;
            case("מרקמים"):
                this.c_gameType="Textures";
                break;
            case("textures"):
                this.c_gameType="Textures";
                break;
            case("משולב"):
                this.c_gameType="Both";
                break;
            case("combined"):
                this.c_gameType="Both";
                break;
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
