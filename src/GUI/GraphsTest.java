package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class GraphsTest extends Application {
    // members
    private double window_height;
    private double window_width;
    private JDialog dialog = new JDialog();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GraphsWindow.fxml"));
        primaryStage.setTitle("MSC");
        setWindowSize();
        Scene main_window = new Scene(root,  this.window_width,  this.window_height);
        primaryStage.setScene(main_window);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
        Connection connection = null;
        try {
            connection = Connection.getInstance();
        } catch (Exception e) {
            Alerter.showAlert(e.getMessage(), Alert.AlertType.ERROR);
            primaryStage.close();
            stop();
            return;
        }
        if(connection == null) {
            primaryStage.close();
            stop();
            return;
        }
        boolean tryAgain = true;
        while(tryAgain) {
            // try to open a connection to the controller
            try {
                connection.OpenConnection();
                tryAgain = false;
            } catch (Exception e) {
                // display appropriate message
                tryAgain = Alerter.showAlert(e.getMessage(), Alert.AlertType.ERROR, "yesno");
                // if user clicked no then close the application
                if(!tryAgain) {
                    primaryStage.close();
                }
            }
        }
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

    @Override
    public void stop() throws Exception {
        try {
            Connection connection = Connection.getInstance();
            // try to close the connection to the controller
            connection.CloseConnection();
        } catch (Exception e) {

        }
        // stop the application
        super.stop();
    }

    public static void main(String[] args) { launch(args); }
}