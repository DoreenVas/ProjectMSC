package GUI;

import Model.Connection;
import Resources.AlertMessages;
import Resources.Alerter;
import Resources.GameContainer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sun.awt.Mutex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.*;
import java.util.stream.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Model.GameQueries;

import javax.imageio.ImageIO;
import javax.swing.*;

/********
 * the window of the game.
 * each round we show an image - the patient must identify the corresponding button
 * and press it.
 * in case of success - we move to the next image, and in case of failure
 * we stay with the same image until the timer is off.
 *
 * Proper indication sounds and images are shown according to the key pressed.
 *
 * After all images are shown, we move to the results window.
 */
public class GameWindow extends BasicWindow {
    // members
    @FXML
    AnchorPane anchorPane;
    @FXML
    private ImageView home;
    @FXML
    private Label timerLabel;
    @FXML
    private ImageView image;
    @FXML
    private Label wellDoneLabel;
    @FXML
    private ImageView indicationImage;
    @FXML
    private Label progressLabel;

    // timer members
    private AnimationTimer timer;
    // time limit = the initial time limit for each image
    private double initialTimeLimit;
    private double timeLimit;
    private String gameType;
    private String dominantHand;

    private double window_height;
    private double window_width;
    //
    private double currTime = 0;
    private DoubleProperty timeLeft = new SimpleDoubleProperty();
    private BooleanProperty running = new SimpleBooleanProperty();
    private IntegerProperty progress = new SimpleIntegerProperty();
    private StringProperty imagesLeftLabel = new SimpleStringProperty();

    // shapes and textures
    private HashMap<String, String> picToPath = new HashMap<>();
    private HashMap<String, String> picToKey = new HashMap<>();
    private HashMap<String, Double> shapesReactionTimes = new HashMap<>();
    private HashMap<String, Double> texturesReactionTimes = new HashMap<>();

    // files and paths
    private String shapesDirPath = "pic/shapes";
    private String texturesDirPath = "pic/textures";
    private String shapesToKeysFilePath = "shapesToKeys";
    private String texturesToKeysFilePath = "texturesToKeys";
    private String tickImagePath = "/pic/misc/Tick.png";
    private String redXImagePath = "/pic/misc/Red_X.png";
    private String applauseImagePath = "/pic/misc/Clapping_Hands2.jpg";
    private String wrongSound = "sounds/Wrong_Answer_Sound_Effect.wav";
    private String correctSound = "sounds/Correct_Answer_Sound_Effect.mp3";
    private String applauseSound = "sounds/Applause.mp3";

    private String currentImage = "";
    private Set imagesSet;
    private boolean timerInitialized = false;
    private boolean nextImage = false;
    private boolean locker = false;
    private Mutex mutex = new Mutex();
    private int numberOfRecognizedImages = 0;
    private boolean resultsWindow = false;
    private boolean applause = true;
    private int sleepTime = 1500; // in milliseconds
    private int imagesSetSize;
    private MediaPlayer mediaPlayer;

    /*******
     * the function initializes the components of the game:
     * the timer, the images to keys maps,
     * the images list and the progress label.
     * @param c_gameType the current game type.
     * @param c_timeLimit the time limit of the current game.
     * @param c_dominantHand indicates if the patient uses his/her dominant hand in the game.
     */
    public void initialize(String c_gameType,String c_timeLimit, String c_dominantHand) {
        this.anchorPane.requestFocus();
        this.wellDoneLabel.setVisible(false);
        this.gameType = c_gameType;
        this.initialTimeLimit = Double.parseDouble(c_timeLimit);
        this.timeLimit = Double.parseDouble(c_timeLimit);
        this.dominantHand = c_dominantHand;

//        this.initialTimeLimit =1;
//        this.timeLimit = 1;

        super.initialize(null, null);
        // initialize the map
        String path;
        switch(gameType){
            case("Shapes"):
                path = shapesDirPath;
                readImagesFromDir(path);
                break;
            case("Textures"):
                path = texturesDirPath;
                readImagesFromDir(path);
                break;
            default:
                path = shapesDirPath;
                readImagesFromDir(path);
                path = texturesDirPath;
                readImagesFromDir(path);
                break;
        }
        initializeKeysMap(this.shapesToKeysFilePath);
        initializeKeysMap(this.texturesToKeysFilePath);

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
                if (timeLeft.getValue() <= 0.05) {
                    new Thread(()-> {
                        mutex.lock();
                        // when the time's up - show indication image, reset timer and switch to next image
                        stop();
                        nextImage = true;
                        timerInitialized = false;
                        pauseTimer(redXImagePath, wrongSound);
                        mutex.unlock();
                    }).start();
                }
            }
        };
        Platform.runLater(() -> {
            // initialize the set of images
            this.imagesSet = this.picToPath.keySet();
            this.imagesSetSize = this.imagesSet.size();
            // images progress label
            this.imagesLeftLabel.bind((this.progress.add(-1)).asString());
            this.progressLabel.textProperty().bind(Bindings.concat(this.imagesLeftLabel)
                    .concat("/" + String.valueOf(this.imagesSetSize)));
            // set the first image
            switchImage();
            resetTimer();
            this.progress.set(this.imagesSetSize - this.imagesSet.size());
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
                this.mutex.unlock();
            } else {
                locker = true;
                this.mutex.unlock();

                // check if the input is a letter
                if (event.getText().matches("[a-zA-Z';./,]")) {
                    // check if the key that was pressed is the correct key for the image
                    if (event.getText().equals(this.picToKey.get(this.currentImage))) {
                        this.numberOfRecognizedImages++;
                        // set timer initialized to false (for the next image)
                        this.timerInitialized = false;
                        this.nextImage = true;
                        // pause the timer and show proper indication image
                        pauseTimer(this.tickImagePath, this.correctSound);
                    } else { // the key that was pressed is the wrong key
                        // pause the timer and show proper indication image
                        pauseTimer(this.redXImagePath, this.wrongSound);
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
    private void pauseTimer(String indicationImagePath, String soundEffectPath) {
        this.timer.stop();
        Image img = null;
        // set the indication image
        URL mapUrl = this.getClass().getResource(indicationImagePath);
        try {
            img = SwingFXUtils.toFXImage(ImageIO.read(mapUrl), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.indicationImage.setImage(img);
        Media sound = new Media(this.getClass().getClassLoader().getSystemResource(soundEffectPath).toExternalForm());
        this.mediaPlayer = new MediaPlayer(sound);
        try {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.play();
                // show the image for 2 seconds
                Thread.sleep(this.sleepTime);
                // reset the indication image
                this.mediaPlayer.stop();
            }
            this.indicationImage.imageProperty().set(null);
            if (this.nextImage) {
                // switch to the next image
                switchImage();
                Platform.runLater(()-> this.progress.set(this.imagesSetSize - this.imagesSet.size()));
                // ends the game when the images set is empty
                if (this.resultsWindow) { return; }
                // set next image to false
                this.nextImage = false;
                // reset the timer limit.
                this.timeLimit = this.initialTimeLimit;
            }
            // check if the patient guessed 1/3 or 2/3 of the images correct
            if ((this.numberOfRecognizedImages == (this.imagesSetSize / 3) ||
                this.numberOfRecognizedImages == (2 * (this.imagesSetSize / 3))) && this.applause) {
                applause();
                this.wellDoneLabel.setVisible(false);
                // reset the timer
                this.currTime = this.initialTimeLimit;
            }
            if(this.numberOfRecognizedImages == ((this.imagesSetSize / 3) + 1) ||
                    this.numberOfRecognizedImages == ((2 * (this.imagesSetSize / 3)) + 1)) {
                this.applause = true;
            }
            // restart the timer
            timer.start();
            // set timer initialized to false (for the next image)
            this.timerInitialized = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (this.mediaPlayer != null) {
                this.mediaPlayer.stop();
            }
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
            BufferedImage buff = null;
            try {
                buff = ImageIO.read(getClass().getResourceAsStream(this.picToPath.get(pic)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            img = SwingFXUtils.toFXImage(buff, null);
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
                    Platform.runLater(()->showResultsWindow());
                }
            }
        }
    }

    /****
     * show applause indication image and sound after 1/3 correct images
     */
    private void applause() {
        this.applause = false;
        this.wellDoneLabel.setVisible(true);
        // don't show the next image
        Image img = this.image.getImage();
        this.sleepTime = 3000;
        this.image.setImage(null);
        // show the applause image
        pauseTimer(this.applauseImagePath, this.applauseSound);
        // continue
        this.image.setImage(img);
        this.sleepTime = 1500;
        this.image.setVisible(true);
    }

    /******
     * After the game ends, we will insert all the game information into the DB,
     * and show a window with avg reaction time and number of recognized images in the last game.
     */
    private void showResultsWindow() {
        this.timer.stop();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        try {
            Connection conn = Connection.getInstance();
            GameContainer gameContainer = new GameContainer(this.shapesReactionTimes, this.texturesReactionTimes,
                    this.numberOfRecognizedImages, (int)this.initialTimeLimit, this.gameType, this.dominantHand);
            // insert the results of the current game into the database
            conn.insertNewGameQuery(gameContainer);
            // switch to results window
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(MainWindow.language+"/ResultsWindow.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage stage = (Stage)this.home.getScene().getWindow();
                // get the size of the screen
                Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                this.window_height = window.height;
                this.window_width = window.width;
                Scene scene = new Scene(root, window.width, window.height);
                scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
                ResultsWindow resultsWindow= loader.getController();
                resultsWindow.initialize(gameContainer.getShapesReactionTime(), gameContainer.getTexturesReactionTime(), gameContainer.getNumOfRecognizedButtons());
                stage.setTitle("Results");
                // set the window size
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setMaximized(true);
                stage.show();
            } catch (Exception e) {
                Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
            }
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
        String fileName;
        try {
            CodeSource src = this.getClass().getProtectionDomain().getCodeSource();
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
                while(true) {
                    ZipEntry entry = zip.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    String name = entry.getName();
                    if (name.endsWith(".png"))
                    if (name.startsWith(picsFilePath) && name.endsWith("png")) { //filter according to the path
                        fileName = name.split("/")[name.split("/").length - 1];
                        if (fileName != null) {
                            if (!fileName.equals("misc")) {
                                // if it is a file, we add it to the map as: key = name of file, value = path to the file
                                if (!this.picToPath.containsKey(fileName)) {
                                    this.picToPath.put(fileName.toLowerCase(), "/" + entry.getName());
                                }
                            }
                        }
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /****
     * the function initializes the map of the keys with <key,value> as name of picture and value on the keyboard
     * @param keysFilePath the path to the pictures directory
     */
    private void initializeKeysMap(String keysFilePath) {
        String row;
        String[] info;
        InputStream keysFileStream = this.getClass().getClassLoader().getResourceAsStream(keysFilePath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(keysFileStream));
            // read the info from the config file
            row = reader.readLine();
            while (row != null) {

                info = row.split(":");
                this.picToKey.put(info[0].toLowerCase() + ".png", info[1].toLowerCase());
                row = reader.readLine();
            }
            reader.close();
            keysFileStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void mainWindow() {
        this.timer.stop();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        super.menuWindow(this.home);
    }

    @FXML
    protected void logout() {
        this.timer.stop();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        super.logout();
    }
}
