package GUI;

import Resources.AlertMessages;
import Resources.Alerter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.awt.*;
import java.util.HashMap;

/******
 * Results window class.
 * Shows the results of the last game - avg reaction time and
 * number of recognized images.
 */
public class ResultsWindow extends BasicWindow{
    // members
    @FXML
    private ImageView home;
    @FXML
    private Button personal_zone;
    @FXML
    private Label avgTime;
    @FXML
    private Label numOfRecognizedImages;

    private double window_height;
    private double window_width;

    /******
     * the function initializes the average time label of the game
     * and the num of recognized images label
     * @param shapesTimes map of reaction times to shapes images
     * @param texturesTimes map of reaction times to textures images
     * @param numOfImages number of recognized images
     */
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
     * @param texturesTimes reaction times for texture images
     * @return the average time of a specific game
     */
    private double calculateAvgReactionTime(HashMap<String, Double> shapesTimes, HashMap<String, Double> texturesTimes) {
        int size = shapesTimes.size() + texturesTimes.size();
        double avg = 0;
        for (Double d : shapesTimes.values()) {
            avg += d;
        }
        for (Double d : texturesTimes.values()) {
            avg += d;
        }
        avg = avg / size;
        return avg;
    }

    /******
     * clicking on the personal zone button will direct the patient to his/her personal zone.
     */
    @FXML
    protected void personal_Zone() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MainWindow.language+"/GraphsWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.personal_zone.getScene().getWindow();
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
