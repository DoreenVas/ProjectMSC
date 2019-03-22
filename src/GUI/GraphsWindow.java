package GUI;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphsWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private NumberAxis game_id;
    @FXML
    private NumberAxis avgReactionTime;
    @FXML
    private JFXComboBox<String> showGlobalAverage = new JFXComboBox();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.game_id.setAutoRanging(false);
        this.game_id.setLowerBound(1);
        this.game_id.setUpperBound(10);
        this.game_id.setTickUnit(1);

        this.avgReactionTime.setAutoRanging(false);
        this.avgReactionTime.setLowerBound(0);
        this.avgReactionTime.setUpperBound(70);
        this.avgReactionTime.setTickUnit(10);

        loadChartData();
    }

    @FXML
    private void loadChartData() {
        //Creating the line chart
        XYChart.Series<Number, Number> series = new XYChart.Series();
        series.setName("ההתקדמות שלי");
        series.getData().add(new XYChart.Data<>(1, 23.4));
        series.getData().add(new XYChart.Data<>(2, 11.4));
        series.getData().add(new XYChart.Data<>(3, 30.4));

        this.lineChart.getData().add(series);
    }

    @FXML
    private void getGlobalAvgGraph() {
        String select = this.showGlobalAverage.getSelectionModel().getSelectedItem().toString();
        System.out.println(select);

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
