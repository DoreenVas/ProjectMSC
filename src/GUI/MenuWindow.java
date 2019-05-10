package GUI;

import Resources.AlertMessages;
import Resources.Alerter;
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

/********
 * Menu window class.
 * Contains the menu of the application, allowing
 * the patient to choose the next operation
 * they want to do:
 * - start a new game.
 * - view the instructions.
 * - view the personal zone.
 * - exit the application.
 */
public class MenuWindow extends BasicWindow implements Initializable{
    @FXML
    private Button start;
    @FXML
    private Button instructions;
    @FXML
    private Button results;

    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // show patient name
        super.initialize(location, resources);

        // allow enter press on start button
        this.start.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                start();
            }
        });
        // allow mouse key click
        this.start.setOnMouseClicked(event -> {
                start();
        });


        // allow enter press on instructions button
        this.instructions.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                instructions();
            }
        });
        // allow mouse key click
        this.instructions.setOnMouseClicked(event -> {
            instructions();
        });

        // allow enter press on instructions button
        this.results.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                results();
            }
        });
        // allow mouse key click
        this.results.setOnMouseClicked(event -> {
            results();
        });
    }

    /******
     * clicking on the instructions button will direct the patient to
     * the instructions window.
     */
    @FXML
    protected void instructions() {
        try {
            Stage stage = (Stage) this.instructions.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindow.language+"/InstructionsWindow.fxml"));
            stage.setTitle("Instructions");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width ,  this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /******
     * clicking on the start game button will direct the patient to
     * the settings window, he/she could determine the settings of the next game.
     */
    @FXML
    protected void start() {
        try {
            Stage stage = (Stage) this.start.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource(MainWindow.language+"/SettingsWindow.fxml"));
            stage.setTitle("Settings");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width,  this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch(Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /******
     * clicking on the results button will direct the patient to
     * the personal zone window.
     */
    @FXML
    protected void results() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MainWindow.language+"/GraphsWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.results.getScene().getWindow();
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width,  this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
            GraphsWindow graphsWindow = loader.getController();
            graphsWindow.setPreviousScene(MainWindow.language+"/MenuWindow.fxml");
            stage.setTitle("Patient Data");
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
