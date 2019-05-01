package GUI;

import Controller.PieChartBuilder;
import Model.Connection;
import Resources.AlertMessages;
import Resources.Alerter;
import Resources.PatientContainer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminWindow implements Initializable{
    @FXML
    private JFXButton submit;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXButton shapePieChart;
    @FXML
    private JFXButton texturesPieChart;
    @FXML
    private JFXButton bothPieChart;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXButton back;

    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // allow enter key press
        this.id.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                submit();
            }
        });
    }

    @FXML
    protected void submit() {
        try {
            // check validation of id number
            int id = Integer.parseInt(this.id.getText());
            if (id < 0 || id > 999999999) {
                throw new NumberFormatException();
            }
            // get the information of the patient
            String idString = this.id.getText();
            Connection conn = Connection.getInstance();
            PatientContainer p_info = conn.idQuery(idString);
            // patient doesn't exist in the database
            if (p_info == null) {
                switch (MainWindow.language) {
                    case "Hebrew":
                        Alerter.showAlert("תעודת זהות לא במערכת. נסה שנית.", Alert.AlertType.WARNING);
                        break;
                    case "English":
                        Alerter.showAlert("Id is not in the system. Try again.", Alert.AlertType.WARNING);
                        break;
                }
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(MainWindow.language+"/GraphsWindow.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage stage = (Stage) this.submit.getScene().getWindow();
                // get the size of the screen
                Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                this.window_height = window.height;
                this.window_width = window.width;
                // set the window size
                Scene scene = new Scene(root,  this.window_width,  this.window_height);
                GraphsWindow graphsWindow = loader.getController();
                graphsWindow.setPreviousScene(MainWindow.language+"/AdminWindow.fxml");
                stage.setTitle("Patient Data");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setMaximized(true);
                stage.show();
            }
        } catch (NumberFormatException e) {
            switch (MainWindow.language) {
                case "Hebrew":
                    Alerter.showAlert("תעודת זהות לא תקינה! נסה שוב.", Alert.AlertType.WARNING);
                    break;
                case "English":
                    Alerter.showAlert("Invalid Id. Try again.", Alert.AlertType.WARNING);
                    break;
            }
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /*****
     * the function shows a pie chart of errors, according to the game type.
     * the game type is chosen by clicking different buttons
     * @param gameType the game type
     */
    private void showPieChart(String gameType) {
        Stage secondStage = new Stage();
        VBox vBox = new VBox();
        AnchorPane anchorPane = new AnchorPane();
        // create the bar chart
        PieChart pieChart = PieChartBuilder.createChart(gameType);
        // add a label for clicking - showing percentage
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double total = 0;
                        for (PieChart.Data d : pieChart.getData()) {
                            total += d.getPieValue();
                        }
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100*data.getPieValue()/total) ;
                        caption.setText(text);
                    }
            );
        }
        // add the chart and the label
        anchorPane.getChildren().addAll(pieChart, caption);
        Scene scene = new Scene(vBox, 600, 600);
        // bind the bar chart to the size of the window
        pieChart.minWidthProperty().bind(anchorPane.widthProperty());
        pieChart.minHeightProperty().bind(anchorPane.heightProperty().subtract(20));
        anchorPane.minWidthProperty().bind(scene.widthProperty());
        anchorPane.minHeightProperty().bind(scene.heightProperty().subtract(20));
        // add a description label to the window
        Label description = new Label();
        switch (MainWindow.language) {
            case "Hebrew":
                description.setText("לחץ על הגרף לקבלת אחוזים");
                break;
            case "English":
                description.setText("Press on the chart to review percentages");
                break;
        }
        description.setFont(Font.font(20));
        // add the chart and the label to the scene
        vBox.getChildren().addAll(anchorPane, description);
        vBox.setAlignment(Pos.CENTER);
        VBox.setMargin(description, new Insets(0, 10, 10, 10));
        // show the window
        secondStage.setScene(scene);
        secondStage.show();
    }

    @FXML
    private void shapesPieChart() {
        showPieChart("Shapes");
    }

    @FXML
    private void texturesPieChart() {
        showPieChart("Textures");
    }

    @FXML
    private void bothPieChart() {
        showPieChart("Both");
    }

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) this.exit.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void back() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MainWindow.language + "/MainWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.submit.getScene().getWindow();
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            stage.setTitle("Patient Data");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }
}
