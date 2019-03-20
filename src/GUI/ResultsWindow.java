package GUI;

import Resources.PatientContainer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ResultsWindow extends BasicWindow{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private Label avgTime = new Label();
    @FXML
    private Label numOfRecognizedImages = new Label();

    public void initialize(HashMap shapesTimes, HashMap texturesTimes, int numOfImages) {
        super.initialize(null, null);
        double avgTime = calculateAvgReactionTime(shapesTimes, texturesTimes);
        this.avgTime.setText(Double.toString(avgTime));
        this.numOfRecognizedImages.setText(Integer.toString(numOfImages));
    }

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
