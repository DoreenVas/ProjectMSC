package GUI;

import Resources.GameContainer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import sun.awt.Mutex;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import Model.GameQueries;

public class GameWindow extends BasicWindow {
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private Label timerLabel = new Label();
    @FXML
    private ImageView image = new ImageView();
    @FXML
    private ImageView indicationImage = new ImageView();

    // timer members
    private AnimationTimer timer;
    // time limit = the initial time limit for each image
    private double initialTimeLimit;
    private double timeLimit;
    private String gameType;
    private String keyboard;
    //
    private double currTime = 0;
    private DoubleProperty timeLeft = new SimpleDoubleProperty();
    private BooleanProperty running = new SimpleBooleanProperty();

    // shapes and textures
    private HashMap<String, String> shapesAndTexturesMap = new HashMap<>();
    private HashMap<String, String> shapesAndKeysMap = new HashMap<>();
    private HashMap<String, Double> shapesReactionTimes = new HashMap<>();
    private HashMap<String, Double> texturesReactionTimes = new HashMap<>();

    // files and paths
    private String picturesDirPath= "src/GUI/pic";
    private String shapesDirPath = "src/GUI/pic/shapes";
    private String texturesDirPath = "src/GUI/pic/textures";
    private String shapesToKeysFilePath = "src/GUI/shapesToKeys";
    private String texturesToKeysFilePath = "src/GUI/texturesToKeys";
    private String tickImagePath = "src/GUI/pic/misc/Tick.png";
    private String redXImagePath = "src/GUI/pic/misc/Red_X.png";

    private String currentImage = "";
    private Set imagesSet;
    private boolean timerInitialized = false;
    private boolean nextImage = false;
    private boolean locker = false;
    private Mutex mutex = new Mutex();
    private int numberOfRecognizedImages = 0;
    private boolean resultsWindow;

    public void initialize(String c_gameType,String c_timeLimit,String c_keyboard) {
        this.gameType = c_gameType;
        this.initialTimeLimit = Double.parseDouble(c_timeLimit);
        this.timeLimit = Double.parseDouble(c_timeLimit);
        this.keyboard = c_keyboard;

        super.initialize(null, null);
        // initialize the shapes and textures map
        this.readImagesFromDir(this.picturesDirPath);
        this.initializeKeysMap(this.shapesToKeysFilePath);
        this.initializeKeysMap(this.texturesToKeysFilePath);
        this.resultsWindow = false;
        //bind the label to the time left
        this.timerLabel.textProperty().bind(this.timeLeft.asString("%.0f "));
        // create the timer
        this.timer = new AnimationTimer() {

            private long startTime;

            @Override
            public void start() {
                this.startTime = System.currentTimeMillis();
                // if the timer was already initialized, it means the timer was paused and we want to return to the
                // current time count.
                if (timerInitialized) {
                    timeLimit = currTime;
                }
                running.set(true);
                super.start();
            }

            @Override
            public void stop() {
                long now = System.currentTimeMillis();
                // save the time that passed
                currTime = timeLimit - ((now - this.startTime) / 1000.0);
                running.set(false);
                super.stop();
            }

            @Override
            public void handle(long timestamp) {
                long now = System.currentTimeMillis();
                // calculate the reminding time: tileLeft = timeLimit - (currentSystemTime - startSystemTime)
                timeLeft.set(timeLimit - ((now - this.startTime) / 1000.0));
                if (timeLeft.getValue() <= 0.01) {
                    new Thread(()-> {
                        mutex.lock();
                        // when the time's up - show indication image, reset timer and switch to next image
                        stop();
                        nextImage = true;
                        timerInitialized = false;
                        pauseTimer(redXImagePath);
                        mutex.unlock();
                    }).start();
                }
            }
        };
        Platform.runLater(() -> {
            // initialize the set of images
            this.imagesSet = this.shapesAndTexturesMap.keySet();
            // set the first image
            switchImage();
            resetTimer();
        });
    }


    /******
     * The function handles the input key from the user
     * @param event the key released of the user
     */
    @FXML
    private void handleRelease(KeyEvent event) {
    }

    /******
     * The function handles the input key from the user
     * @param event the key press of the user
     */
    @FXML
    private void handlePress(KeyEvent event) {
        new Thread(() -> {
            this.mutex.lock();
            // if locker is locked, we already have a key press in handle
            if (locker) {
                System.out.println("Discarded");
                this.mutex.unlock();
            } else {
                locker = true;
                this.mutex.unlock();

                // check if the input is a letter
                if (event.getText().matches("[a-zA-Z';./,]")) {
                    // check if the key that was pressed is the correct key for the image
                    if (event.getText().equals(this.shapesAndKeysMap.get(this.currentImage))) {
                        System.out.println("Correct Key!");
                        this.numberOfRecognizedImages++;
                        // set timer initialized to false (for the next image)
                        this.timerInitialized = false;
                        this.nextImage = true;
                        // pause the timer and show proper indication image
                        pauseTimer(this.tickImagePath);
                    } else { // the key that was pressed is the wrong key
                        System.out.println("Wrong Key...try again");
                        // pause the timer and show proper indication image
                        pauseTimer(this.redXImagePath);
                    }
                }
                locker = false;
            }
        }).start();
    }

    /****
     * the function pauses the timer, shows a proper indication image and continues the timer.
     * @param indicationImagePath the path to the indication image
     */
    public void pauseTimer(String indicationImagePath) {
        this.timer.stop();
        Image img;
        // set the indication image
        img = new Image(new File(indicationImagePath).toURI().toString());
        this.indicationImage.setImage(img);
        try {
            // show the image for 1 seconds
            Thread.sleep(1000);
            // reset the indication image
            this.indicationImage.imageProperty().set(null);
            if (this.nextImage) {
                // switch to the next image
                switchImage();
                // set next image to false
                this.nextImage = false;
                /////// reset the timer limit. TODO initialize from settings
                this.timeLimit = this.initialTimeLimit;
            }
            // restart the timer
            timer.start();
            // set timer initialized to false (for the next image)
            this.timerInitialized = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*****
     * the function resets the timer
     */
    private void resetTimer() {
        this.timeLimit = this.initialTimeLimit;
        this.timerInitialized = false;
        // start the timer
        this.timer.start();
        // set the timer as initialized
        this.timerInitialized = true;
    }

    /*****
     * the function switches between the images.
     * when the images end (pic == null) goes to the results window.
     */
    private void switchImage() {
        // initialize the number of recognized images
        this.numberOfRecognizedImages = 0;
        if (this.currentImage != null) {
            String imageType = GameQueries.getImageType(this.currentImage.replace(".png", ""));
            if (imageType != null) {
                // calculate the reaction time
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                Double reactionTime = Double.valueOf(decimalFormat.format(this.initialTimeLimit - this.currTime));
                // insert the reaction time
                switch (imageType){
                    case "Shapes":
                        this.shapesReactionTimes.put(this.currentImage, reactionTime);
                        break;
                    case "Textures":
                        this.texturesReactionTimes.put(this.currentImage, reactionTime);
                        break;
                    case "Both":
                        this.shapesReactionTimes.put(this.currentImage, reactionTime);
                        this.texturesReactionTimes.put(this.currentImage, reactionTime);
                        break;
                }
            }
        }
        Image img;
        // make a list from the images set
        List<String> listOfImg = new ArrayList<>(this.imagesSet);
        // shuffle the list
        Collections.shuffle(listOfImg);
        // take the first image in the list
        String pic;
        if (listOfImg.size() > 0) {
            pic = listOfImg.get(0);
        } else {
            pic = null;
        }
        if (pic != null) {
            // change to the next image
            img = new Image(new File(this.shapesAndTexturesMap.get(pic)).toURI().toString());
            this.image.setImage(img);
            this.currentImage = pic;
            // remove the image from the set
            this.imagesSet.remove(pic);
        } else {
            // check if the images set is empty and we finished with the last image
            if (this.imagesSet.isEmpty()) {
                this.resultsWindow = true;
                // if the images set is empty -  move to the results page
                if (this.resultsWindow){
                    showResultsWindow();
                }
            }
        }
    }

    private void showResultsWindow() {
        try {
            Connection conn = Connection.getInstance();
            GameContainer gameContainer = new GameContainer(this.shapesReactionTimes, this.texturesReactionTimes,
                    this.numberOfRecognizedImages, (int)this.initialTimeLimit, this.gameType);
            // insert the results of the current game into the database
            conn.insertNewGameQuery(gameContainer);
            // TODO go to results window
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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
        if(listOfFiles != null) {
            for (File file : listOfFiles) {
                if (!file.getName().equals("misc")) {
                    if (file.isFile()) { // if it is a file, we add it to the map as: key = name of file, value = path to the file
                        if (!this.shapesAndTexturesMap.containsKey(file.getName())) {
                            this.shapesAndTexturesMap.put(file.getName().toLowerCase(), file.getAbsolutePath());
                        }
                    } else { // if it is a directory we will go over the file inside of it
                        readImagesFromDir(file.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("Not More Files!");
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
            while (row != null) {

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
