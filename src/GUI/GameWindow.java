package GUI;

import Resources.PatientContainer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private Label timerLabel = new Label();
    private Timer timer;
    private int currentTimeLimit = 30;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // decrease the time that passed by 1 second
                currentTimeLimit--;
                timerLabel.setText(String.valueOf(currentTimeLimit));
            }
        };
    }

    @FXML
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    public void logout() {
        super.logout();
    }
}
