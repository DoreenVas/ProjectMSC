package GUI;

import Resources.GameContainer;
import Resources.PatientContainer;
import Resources.TableInfoContainer;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<TableInfoContainer> resultsTable;

    private ArrayList<XYChart.Series> allPatientsRegressionLine;
    private ArrayList<XYChart.Series> myReactionTimes;
    private HashMap<String, ArrayList<XYChart.Series>> timesToSeriesMap;

    private static String[] shapesColumns = {"arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private static String[] texturesColumns = {"four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // show patient name
        super.initialize(location, resources);

        // initialize lists and checkbox
        this.allPatientsRegressionLine = new ArrayList<>();
        this.myReactionTimes = new ArrayList<>();
        this.timesToSeriesMap = new HashMap<>();
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
            ArrayList<GameContainer> games = conn.getGames(PatientContainer.getInstance().getPatientID());
            createTable(games);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        this.lineChart.getData().add(series);
    }

    private void createTable(ArrayList<GameContainer> gamesList) {
//        TableColumn<SongDisplayData, String> column = new TableColumn<>(field);
//        column.setCellValueFactory(new PropertyValueFactory<>(field));

        TableColumn<TableInfoContainer, String> gameTypeCol = new TableColumn<>("gameType");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory<>("gameType"));
        gameTypeCol.setText("סוג משחק");
        this.resultsTable.getColumns().add(gameTypeCol);

        TableColumn<TableInfoContainer, String> timeLimitCol = new TableColumn<>("timeLimit");
        timeLimitCol.setCellValueFactory(new PropertyValueFactory<>("timeLimit"));
        timeLimitCol.setText("מגבלת הזמן");
        this.resultsTable.getColumns().add(timeLimitCol);

        TableColumn<TableInfoContainer, String> recognizedCol = new TableColumn<>("numOfRecognizedButtons");
        recognizedCol.setCellValueFactory(new PropertyValueFactory<>("numOfRecognizedButtons"));
        recognizedCol.setText("מספר התמונות שזוהו");
        this.resultsTable.getColumns().add(recognizedCol);

//        TableColumn<GameContainer, String> dateCol = new TableColumn<>("תאריך המשחק");
//        gameTypeCol.setCellValueFactory(new PropertyValueFactory("gameDate"));

        for (String s : shapesColumns) {
            TableColumn<TableInfoContainer, String> shapesColumn = new TableColumn<>(s);
            shapesColumn.setCellValueFactory(new PropertyValueFactory<>(s));
            this.resultsTable.getColumns().add(shapesColumn);
        }
        for (String s : texturesColumns) {
            TableColumn<TableInfoContainer, String> texturesColumn = new TableColumn<>(s);
            texturesColumn.setCellValueFactory(new PropertyValueFactory<>(s));
            this.resultsTable.getColumns().add(texturesColumn);
        }

        // add columns to the table
        this.resultsTable.getItems().addAll(getGames(gamesList));
    }

    private ObservableList<TableInfoContainer> getGames(ArrayList<GameContainer> list) {
        ObservableList<TableInfoContainer> games = FXCollections.observableArrayList();
        for (GameContainer g : list) {
            TableInfoContainer t = new TableInfoContainer();
            t.setGameType(g.getGameType()).setTimeLimit(String.valueOf(g.getTimeLimit()))
                    .setNumOfRecognizedButtons(String.valueOf(g.getNumOfRecognizedButtons()));
            for (String s : shapesColumns) {
                insertInfoToTableContainer(t, g, s);
            }
            for (String s : texturesColumns) {
                insertInfoToTableContainer(t, g, s);
            }
            games.add(t);
        }
        return games;
    }

    private void insertInfoToTableContainer(TableInfoContainer t, GameContainer g, String field) {
        switch (field) {
            case "arrow":
                t.setArrow(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "rectangle":
                t.setRectangle(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "diamond":
                t.setDiamond(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "pie":
                t.setPie(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "triangle":
                t.setTriangle(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "heart":
                t.setHeart(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "flower":
                t.setFlower(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "hexagon":
                t.setHexagon(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "moon":
                t.setMoon(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "plus":
                t.setPlus(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "oval":
                t.setOval(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "two_triangles":
                t.setTwo_triangles(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "circle":
                t.setCircle(String.valueOf(g.getShapesReactionTime().get(field)));
                break;
            case "star":
                t.setStar(String.valueOf(g.getShapesReactionTime().get(field)));
                break;

            case "four_dots":
                t.setFour_dots(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "waves":
                t.setWaves(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "arrow_head":
                t.setArrow_head(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "strips":
                t.setStrips(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "happy_smiley":
                t.setHappy_smiley(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "spikes":
                t.setSpikes(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "dollar":
                t.setDollar(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "net":
                t.setNet(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "note":
                t.setNote(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "arcs":
                t.setArcs(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "monitor":
                t.setMonitor(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "sad_smiley":
                t.setSad_smiley(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "strudel":
                t.setStrudel(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "four_bubbles":
                t.setFour_bubbles(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "spiral":
                t.setSpiral(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
            case "squares":
                t.setSquares(String.valueOf(g.getTexturesReactionTime().get(field)));
                break;
        }
        //return t;
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
