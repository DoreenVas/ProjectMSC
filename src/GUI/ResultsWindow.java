package GUI;

import Resources.AlertMessages;
import Resources.Alerter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;

public class ResultsWindow extends BasicWindow{
    // members
    @FXML
    private Button home;
    @FXML
    private Button exit;
    @FXML
    private Button personal_zone;
    @FXML
    private Label avgTime;
    @FXML
    private Label numOfRecognizedImages;

    private double window_height;
    private double window_width;

    public void initialize(HashMap<String, Double> shapesTimes, HashMap<String, Double> texturesTimes, int numOfImages) {
        super.initialize(null, null);
        double avgTime = calculateAvgReactionTime(shapesTimes, texturesTimes);
        int size = shapesTimes.size() + texturesTimes.size();
        this.avgTime.setText(String.format("%.3f", avgTime));
        this.numOfRecognizedImages.setText(Integer.toString(numOfImages) + "/" + size);
    }

    /*****
     * the function get maps of all the images to reactions times in a specific game,
     * and calculates the average reaction time of that game
     * @param shapesTimes reaction times for shapes images
     * @param textuesTimes reaction times for texture images
     * @return the average time
     */
    private double calculateAvgReactionTime(HashMap<String, Double> shapesTimes, HashMap<String, Double> textuesTimes) {
        int size = shapesTimes.size() + textuesTimes.size();
        double avg = 0;
        for (Double d : shapesTimes.values()) {
            avg += d;
        }
        for (Double d : textuesTimes.values()) {
            avg += d;
        }
        avg = avg / size;
        return avg;
    }

    @FXML
    protected void personal_Zone() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("GraphsWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.personal_zone.getScene().getWindow();
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root,  this.window_width,  this.window_height);
            GraphsWindow graphsWindow = loader.getController();
            graphsWindow.setPreviousScene("MenuWindow.fxml");
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
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    @FXML
    protected void logout() {
        super.logout();
    }

    @FXML
    protected void exit() {
        super.exit();
    }
}
