package GUI;

import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
    @FXML
    private ImageView indicationImage = new ImageView();

    private AnimationTimer timer;
    private double timeLimit = 10;
    private DoubleProperty timeLeft = new SimpleDoubleProperty();
    private BooleanProperty running = new SimpleBooleanProperty();

    private HashMap<String, String> shapesAndTexturesMap = new HashMap<>();
    private HashMap<String, String> shapesAndKeysMap = new HashMap<>();

    private String picturesDirPath = "projectmsc/src/GUI/pic";
    private String shapesToKeysFilePath = "projectmsc/src/GUI/shapesToKeys";
    private String texturesToKeysFilePath = "projectmsc/src/GUI/texturesToKeys";
    private String tickImagePath = "projectmsc/src/GUI/pic/Tick.png";
    private String redXImagePath = "projectmsc/src/GUI/pic/Red_X.png";

    private String currentImage = "";
    private boolean nextImage = false;

    /******
     * The function handles the input key from the user
     * @param event the key press of the user
     */
    @FXML
    private void handle(KeyEvent event) {
        if (event.getText().equals(this.shapesAndKeysMap.get(this.currentImage))) {
            System.out.println("Correct Key!");
            pauseTimer(this.tickImagePath);
        } else {
            System.out.println("Wrong Key...try again");
            pauseTimer(this.redXImagePath);
        }
    }

    public void pauseTimer(String indicationImagePath) {
        this.timer.stop();
        Image img;
        // set the indication image
        img = new Image(new File(indicationImagePath).toURI().toString());
        this.indicationImage.setImage(img);
        Platform.runLater(() -> {
            try {
                Thread.sleep(2000);
                this.indicationImage.imageProperty().set(null);
                timer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        // initialize the shapes and textures map
        this.readImagesFromDir(this.picturesDirPath);
        this.initializeKeysMap(this.shapesToKeysFilePath);
        this.initializeKeysMap(this.texturesToKeysFilePath);

        //bind the label to the time left
        this.timerLabel.textProperty().bind(this.timeLeft.asString("%.0f "));
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
                if (timeLeft.getValue() <= 0) {
                    stop();
                    nextImage = true;
                }
            }
        };
        startGame();
    }

    private void startGame() {
        Image img;
        for (String pic : this.shapesAndTexturesMap.keySet()) {
            // set the current image
            this.currentImage = pic;
            img = new Image(new File(this.shapesAndTexturesMap.get(pic)).toURI().toString());
            this.image.setImage(img);
            // start the timer
            this.timer.start();
            //TODO fix showing the pictures
//            while(!this.nextImage) {
//
//            }
            // reset the next image to false
            this.nextImage = false;
        }
    }

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
                    this.shapesAndTexturesMap.put(file.getName().toLowerCase(), file.getAbsolutePath());
                }
            } else { // if it is a directory we will go over the file inside of it
                readImagesFromDir(file.getAbsolutePath());
            }
        }
    }

    /****
     * the function initializes the map of the keys with <key,value> as name of picture and value on the keyboard
     * @param keysFilePath the path to the pictures directory
     */
    private void initializeKeysMap(String keysFilePath) {
        String row;
        String[] info;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(keysFilePath));
            // read the info from the config file
            row = reader.readLine();
            while(row != null) {

                info = row.split(":");
                this.shapesAndKeysMap.put(info[0].toLowerCase() + ".png", info[1].toLowerCase());
                row = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open keys file reader\n");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read from keys file\n");
            e.printStackTrace();
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
