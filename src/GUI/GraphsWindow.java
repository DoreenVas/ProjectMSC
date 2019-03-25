package GUI;

import Resources.AlertMessages;
import Resources.GameContainer;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphsWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button back;
    @FXML
    private LineChart<?, ?> lineChart;
    @FXML
    private NumberAxis game_id;
    @FXML
    private NumberAxis avgReactionTime;
    @FXML
    private JFXCheckBox showGlobalAverage = new JFXCheckBox();
    @FXML
    private TableView<GameContainer> resultsTable = new TableView();

    private ArrayList<XYChart.Series> allPatientsRegressionLine;
    private ArrayList<XYChart.Series> myReactionTimes;
    private HashMap<String, ArrayList<XYChart.Series>> timesToSeriesMap;

    private String previousScene;
    private double window_height;
    private double window_width;

    private static String[] shapesColumns = {"arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private static String[] texturesColumns = {"four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};

    public void setPreviousScene(String prevScene){
        this.previousScene = prevScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // show patient name
        super.initialize(location, resources);

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
//        try {
//            Connection conn = Connection.getInstance();
//            conn.OpenConnection();
//            ArrayList<GameContainer> games = conn.getGames("2");
//            createTable(games);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        this.lineChart.getData().add(series);
    }

    private void createTable(ArrayList<GameContainer> gamesList) {
        TableColumn<GameContainer, String> gameTypeCol = new TableColumn<>("סוג משחק");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory("gameType"));
        TableColumn<GameContainer, Integer> timeLimitCol = new TableColumn<>("זמן המשחק");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory("timeLimit"));
        TableColumn<GameContainer, Integer> recognizedCol = new TableColumn<>("מספר תמונות שזוהו");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory("numOfRecognizedButtons"));
//        TableColumn<GameContainer, String> dateCol = new TableColumn<>("תאריך המשחק");
//        gameTypeCol.setCellValueFactory(new PropertyValueFactory("gameDate"));

        // add columns to the table
        this.resultsTable.setItems(getGames(gamesList));
        //this.resultsTable.getColumns().addAll(gameTypeCol, timeLimitCol, recognizedCol);

        for (String s : shapesColumns) {
            TableColumn<Map, Double> shapesColumn = new TableColumn<>(s);
            shapesColumn.setCellValueFactory(new MapValueFactory(s));
        }

    }

    private ObservableList<GameContainer> getGames(ArrayList<GameContainer> list) {
        ObservableList<GameContainer> games = FXCollections.observableArrayList();
        games.addAll(list);
        return games;
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
    protected void back() {
        try {
            Stage stage = (Stage) this.back.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource(previousScene));
            stage.setTitle("MSC");
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void logout() {
        super.logout();
    }
}
