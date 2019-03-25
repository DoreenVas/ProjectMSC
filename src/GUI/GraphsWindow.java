package GUI;

import Resources.GameContainer;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GraphsWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button home = new Button();
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private NumberAxis game_id;
    @FXML
    private NumberAxis avgReactionTime;
    @FXML
    private JFXCheckBox showGlobalAverage = new JFXCheckBox();
    @FXML
    private TableView resultsTable = new TableView();

    private ArrayList<XYChart.Series> allPatientsRegressionLine;
    private ArrayList<XYChart.Series> myReactionTimes;
    private HashMap<String, ArrayList<XYChart.Series>> timesToSeriesMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize lists and checkbox
        this.allPatientsRegressionLine = new ArrayList<>();
        this.myReactionTimes = new ArrayList<>();
        this.timesToSeriesMap = new HashMap<>();
        this.resultsTable = new TableView();
        this.showGlobalAverage.setSelected(false);

        loadChartData();

        this.game_id.setAutoRanging(false);
        this.game_id.setLowerBound(1);
        this.game_id.setUpperBound(10);
        this.game_id.setTickUnit(1);

        this.avgReactionTime.setAutoRanging(false);
        this.avgReactionTime.setLowerBound(0);
        this.avgReactionTime.setUpperBound(70);
        this.avgReactionTime.setTickUnit(10);


    }

    @FXML
    private void loadChartData() {
        //Creating the line chart
//        XYChart.Series series = new XYChart.Series();
//        series.setName("ההתקדמות שלי");
//        series.getData().add(new XYChart.Data<>(1, 23.4));
//        series.getData().add(new XYChart.Data<>(2, 11.4));
//        series.getData().add(new XYChart.Data<>(3, 30.4));
        try {
            Connection conn = Connection.getInstance();
            conn.OpenConnection();
            ArrayList<GameContainer> games = conn.getGames("2");
            System.out.println("Shimon");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        this.lineChart.getData().add(series);
    }

    @FXML
    private void getGlobalAvgGraph() {
        boolean select = this.showGlobalAverage.isSelected();
        // if the checkbox is selected - get all information of games from the DB
        if (select) {

        } else { // leave only the patient's results
            for (XYChart.Series s : this.allPatientsRegressionLine) {
                this.lineChart.getData().remove(s);
            }
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
