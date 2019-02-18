package GUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.*;

public class Main extends Application {
    // members
    private double window_height;
    private double window_width;
    private JDialog dialog = new JDialog();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("MSC");
//         get the size of the screen
//        Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//        this.window_height = window.height;
//        this.window_width = window.width;
//         set the window size
        setWindowSize();
        Scene main_window = new Scene(root,  this.window_width,  this.window_height);
        primaryStage.setScene(main_window);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void setWindowSize(){
        //size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //height of the task bar
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(dialog.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;

        //available size of the screen
        this.window_width = screenSize.width - dialog.getWidth();
        this.window_height = screenSize.height - taskBarSize - dialog.getHeight();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
