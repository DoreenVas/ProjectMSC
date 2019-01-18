package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {
    // members
    private double window_height;
    private double window_width;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("MSC");
        // get the size of the screen
        Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.window_height = window.height;
        this.window_width = window.width;
        // set the window size
        Scene main_window = new Scene(root,  this.window_width - 5,  this.window_height- 15);
        primaryStage.setScene(main_window);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
