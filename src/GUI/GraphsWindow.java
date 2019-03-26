package GUI;

import Resources.AlertMessages;
import Resources.GameContainer;
import Resources.PatientContainer;
import Resources.TableInfoContainer;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GraphsWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private Button back;
    @FXML
    private LineChart<?, ?> shapesLineChart;
    @FXML
    private NumberAxis shapes_game_id;
    @FXML
    private NumberAxis shapes_avgReactionTime;
    @FXML
    private LineChart<?, ?> texturesLineChart;
    @FXML
    private NumberAxis textures_game_id;
    @FXML
    private NumberAxis textures_avgReactionTime;
    @FXML
    private JFXCheckBox showGlobalAverage = new JFXCheckBox();
    @FXML
    private TableView<TableInfoContainer> resultsTable;

//    private ArrayList<XYChart.Series> allPatientsRegressionLine;
//    private ArrayList<XYChart.Series> myReactionTimes;
    private HashMap<String, XYChart.Series> shapesSeries;
    private HashMap<String, XYChart.Series> texturesSeries;

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
        int index[];
        // initialize lists and checkbox
//        this.allPatientsRegressionLine = new ArrayList<>();
//        this.myReactionTimes = new ArrayList<>();
        this.shapesSeries = new HashMap<>();
        this.texturesSeries = new HashMap<>();
        this.showGlobalAverage.setSelected(false);

        index = loadChartData();

        this.shapes_game_id.setAutoRanging(false);
        this.shapes_game_id.setLowerBound(1);
        this.shapes_game_id.setUpperBound(index[0]);
        this.shapes_game_id.setTickUnit(1);

        this.shapes_avgReactionTime.setAutoRanging(false);
        this.shapes_avgReactionTime.setLowerBound(0);
        this.shapes_avgReactionTime.setUpperBound(70);
        this.shapes_avgReactionTime.setTickUnit(10);

        this.textures_game_id.setAutoRanging(false);
        this.textures_game_id.setLowerBound(1);
        this.textures_game_id.setUpperBound(index[1]);
        this.textures_game_id.setTickUnit(1);

        this.textures_avgReactionTime.setAutoRanging(false);
        this.textures_avgReactionTime.setLowerBound(0);
        this.textures_avgReactionTime.setUpperBound(70);
        this.textures_avgReactionTime.setTickUnit(10);
    }

    @FXML
    private int[] loadChartData() {
        HashMap<String, Integer> shapesReactionTimesCounter = new HashMap<>();
        HashMap<String, Integer> texturesReactionTimesCounter = new HashMap<>();
        int i[] = new int[2];
        try {
            Connection conn = Connection.getInstance();
            // get all the games info
            ArrayList<GameContainer> games = conn.getGames(PatientContainer.getInstance().getPatientID());
            // create the reaction times table of the patient
            createTable(games);
            double avg;
            for (GameContainer g : games) {
                switch (g.getGameType()) {
                    case "Shapes":
                        avg = calculateAvgReactionTimeForGraph(g.getShapesReactionTime());
                        if (!this.shapesSeries.containsKey(String.valueOf(g.getTimeLimit()))) {
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), 1);
                            this.shapesSeries.put(String.valueOf(g.getTimeLimit()), new XYChart.Series());
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).setName(g.getTimeLimit() + " שניות");
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        } else {
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        }
                        break;
                    case "Textures":
                        avg = calculateAvgReactionTimeForGraph(g.getTexturesReactionTime());
                        if (!this.texturesSeries.containsKey(String.valueOf(g.getTimeLimit()))) {
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), 1);
                            this.texturesSeries.put(String.valueOf(g.getTimeLimit()), new XYChart.Series());
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).setName(g.getTimeLimit() + " שניות");
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        } else {
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        }
                        break;
                    case "Both":
                        avg = calculateAvgReactionTimeForGraph(g.getShapesReactionTime());
                        if (!this.shapesSeries.containsKey(String.valueOf(g.getTimeLimit()))) {
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), 1);
                            this.shapesSeries.put(String.valueOf(g.getTimeLimit()), new XYChart.Series());
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).setName(g.getTimeLimit() + " שניות");
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        } else {
                            this.shapesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            shapesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), shapesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        }
                        avg = calculateAvgReactionTimeForGraph(g.getTexturesReactionTime());
                        if (!this.texturesSeries.containsKey(String.valueOf(g.getTimeLimit()))) {
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), 1);
                            this.texturesSeries.put(String.valueOf(g.getTimeLimit()), new XYChart.Series());
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).setName(g.getTimeLimit() + " שניות");
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        } else {
                            this.texturesSeries.get(String.valueOf(g.getTimeLimit())).getData().add(new XYChart.Data<>(texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())), avg));
                            texturesReactionTimesCounter.put(String.valueOf(g.getTimeLimit()), texturesReactionTimesCounter.get(String.valueOf(g.getTimeLimit())) + 1);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // add the series to the charts
        for (XYChart.Series s : this.shapesSeries.values()) {
            this.shapesLineChart.getData().addAll(s);
        }
        for (XYChart.Series s : this.texturesSeries.values()) {
            this.texturesLineChart.getData().addAll(s);
        }
        // calculate the xAxis size of the charts
        i[0] = max(shapesReactionTimesCounter);
        i[1] = max(texturesReactionTimesCounter);
        return i;
    }

    /****
     * the function returns the max value in a map
     * @param map a map
     * @return a max integer in the map
     */
    private int max(HashMap<String, Integer> map) {
        int max = Integer.MIN_VALUE;
        for(Integer i : map.values()) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    private double calculateAvgReactionTimeForGraph(HashMap<String, Double> map) {
        double avg = 0;
        for (Double d : map.values()) {
            avg += d;
        }
        avg = avg / map.values().size();
        return avg;
    }

    private void createTable(ArrayList<GameContainer> gamesList) {
        // create game type column
        TableColumn<TableInfoContainer, String> gameTypeCol = new TableColumn<>("gameType");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory<>("gameType"));
        gameTypeCol.setText("סוג משחק");
        this.resultsTable.getColumns().add(gameTypeCol);

        // create time limit column
        TableColumn<TableInfoContainer, String> timeLimitCol = new TableColumn<>("timeLimit");
        timeLimitCol.setCellValueFactory(new PropertyValueFactory<>("timeLimit"));
        timeLimitCol.setText("מגבלת הזמן");
        this.resultsTable.getColumns().add(timeLimitCol);

        // create number of recognized images column
        TableColumn<TableInfoContainer, String> recognizedCol = new TableColumn<>("numOfRecognizedButtons");
        recognizedCol.setCellValueFactory(new PropertyValueFactory<>("numOfRecognizedButtons"));
        recognizedCol.setText("מספר התמונות שזוהו");
        this.resultsTable.getColumns().add(recognizedCol);

        // create game date column
        TableColumn<TableInfoContainer, String> dateCol = new TableColumn<>("gameDate");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("gameDate"));
        dateCol.setText("תאריך המשחק");
        this.resultsTable.getColumns().add(dateCol);

        // create column for every image
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

    /****
     * the function makes an observable list of table info container (for the table view)
     * @param list a list of game containers
     * @return an observable list
     */
    private ObservableList<TableInfoContainer> getGames(ArrayList<GameContainer> list) {
        ObservableList<TableInfoContainer> games = FXCollections.observableArrayList();
        // go over the games
        for (GameContainer g : list) {
            TableInfoContainer t = new TableInfoContainer();
            // set the game info
            t.setGameType(g.getGameType()).setTimeLimit(String.valueOf(g.getTimeLimit())).setGameDate(g.getGameDate())
                    .setNumOfRecognizedButtons(String.valueOf(g.getNumOfRecognizedButtons()));
            // add the shapes times
            for (String s : shapesColumns) {
                insertInfoToTableContainer(t, g, s);
            }
            // add the textures times
            for (String s : texturesColumns) {
                insertInfoToTableContainer(t, g, s);
            }
            // add to the list
            games.add(t);
        }
        return games;
    }

    /*****
     * insert the reaction time to the tableInfoContainer
     * @param t the tableInfoContainer
     * @param g the gameContainer
     * @param field the image
     */
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

//    @FXML
//    private void getGlobalAvgGraph() {
//        boolean select = this.showGlobalAverage.isSelected();
//        // if the checkbox is selected - get all information of games from the DB
//        if (select) {
//
//        } else { // leave only the patient's results
//            for (XYChart.Series s : this.allPatientsRegressionLine) {
//                this.lineChart.getData().remove(s);
//            }
//        }
//    }

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
