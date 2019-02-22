package GUI;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GameWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private Label timerLabel = new Label();
    @FXML
    private ImageView image = new ImageView();

    private AnimationTimer timer;
    private double timeLimit = 10;
    private DoubleProperty timeLeft = new SimpleDoubleProperty();
    private BooleanProperty running = new SimpleBooleanProperty();

    private HashMap<String, String> shapesAndTexturesMap = new HashMap<>();
    private String picturesDirPath = "projectmsc/src/GUI/pic";

    /****
     * the function initializes the map of the pictures with <key,value> as name of picture and absolute path to the file
     * @param picsFilePath the path to the pictures directory
     */
    private void readImagesFromDir(String picsFilePath) {
        // open the pics folder
        File folder = new File(picsFilePath);
        // get all the files in the directory
        File[] listOfFiles = folder.listFiles();
        // go over the files
        for (File file : listOfFiles) {
            if (file.isFile()) { // if it is a file, we add it to the map as: key = name of file, value = path to the file
                if (!this.shapesAndTexturesMap.containsKey(file.getName()) && !file.getName().equals("MSC.PNG")) {
                    this.shapesAndTexturesMap.put(file.getName(), file.getAbsolutePath());
                }
            } else { // if it is a directory we will go over the file inside of it
                readImagesFromDir(file.getAbsolutePath());
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize the shapes and textures map
        this.readImagesFromDir(this.picturesDirPath);
        // bind the label to the time left
        this.timerLabel.textProperty().bind(this.timeLeft.asString("%.0f seconds"));
        // create the timer
        this.timer = new AnimationTimer() {

            private long startTime ;

            @Override
            public void start() {
                this.startTime = System.currentTimeMillis();
                running.set(true);
                super.start();
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
            }

            @Override
            public void handle(long timestamp) {
                long now = System.currentTimeMillis();
                // calculate the reminding time: tileLeft = timeLimit - (currentSystemTime - startSystemTime)
                timeLeft.set(timeLimit - ((now - this.startTime) / 1000.0));
            }
        };
        startGame();
    }

    private void startGame() {
        Image img;
        for (String pic : this.shapesAndTexturesMap.keySet()) {
            img = new Image(new File(this.shapesAndTexturesMap.get(pic)).toURI().toString());
            this.image.setImage(img);
            // start the timer
            this.timer.start();
            //TODO fix showing the pictures
            this.timer.stop();
        }
    }

    @FXML
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    public void logout() {
        super.logout();
    }
}
