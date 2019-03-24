package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.HashMap;

public class ResultsWindow extends BasicWindow{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private Label avgTime = new Label();
    @FXML
    private Label numOfRecognizedImages = new Label();

    public void initialize(HashMap<String, Double> shapesTimes, HashMap<String, Double> texturesTimes, int numOfImages) {
        super.initialize(null, null);
        double avgTime = calculateAvgReactionTime(shapesTimes, texturesTimes);
        int size = shapesTimes.size() + texturesTimes.size();
        this.avgTime.setText(Double.toString(avgTime));
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
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    @FXML
    protected void logout() {
        super.logout();
    }
}
