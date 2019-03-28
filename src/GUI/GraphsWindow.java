package GUI;

import Model.Connection;
import Resources.*;
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
    private TableView<TableInfoContainer> resultsTable;

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
        int index[] = null;
        // initialize maps
        this.shapesSeries = new HashMap<>();
        this.texturesSeries = new HashMap<>();

        index = loadChartData();
        // check if we received any games
        if (index == null) {
            return;
        }
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
            if (games == null) {
                return null;
            }
            // create the reaction times table of the patient
            createTable(games);
            for (GameContainer g : games) {
                switch (g.getGameType()) {
                    case "Shapes":
                        shapesReactionTimesCounter = insertToSeries(g, shapesReactionTimesCounter, imageTypeEnum.SHAPES.getType());
                        break;
                    case "Textures":
                        texturesReactionTimesCounter = insertToSeries(g, texturesReactionTimesCounter, imageTypeEnum.TEXTURES.getType());
                        break;
                    case "Both":
                        shapesReactionTimesCounter = insertToSeries(g, shapesReactionTimesCounter, imageTypeEnum.SHAPES.getType());
                        texturesReactionTimesCounter = insertToSeries(g, texturesReactionTimesCounter, imageTypeEnum.TEXTURES.getType());
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    private HashMap<String, Integer> insertToSeries(GameContainer g,
                                                   HashMap<String, Integer> timesCounterMap, String gType) {
        String timeLimit = String.valueOf(g.getTimeLimit());
        double avg;

        switch (gType) {
            case "Shapes":
                if (!timesCounterMap.containsKey(timeLimit)) {
                    timesCounterMap.put(timeLimit, 1);
                    this.shapesSeries.put(timeLimit, new XYChart.Series());
                    this.shapesSeries.get(timeLimit).setName(g.getTimeLimit() + " שניות");
                }
                avg = calculateAvgReactionTimeForGraph(g.getShapesReactionTime());
                this.shapesSeries.get(timeLimit).getData().add(new XYChart.Data<>(timesCounterMap.get(timeLimit), avg));
                break;
            case "Textures":
                if (!timesCounterMap.containsKey(timeLimit)) {
                    timesCounterMap.put(timeLimit, 1);
                    this.texturesSeries.put(timeLimit, new XYChart.Series());
                    this.texturesSeries.get(timeLimit).setName(g.getTimeLimit() + " שניות");
                }
                avg = calculateAvgReactionTimeForGraph(g.getTexturesReactionTime());
                this.texturesSeries.get(timeLimit).getData().add(new XYChart.Data<>(timesCounterMap.get(timeLimit), avg));
                break;
        }
        timesCounterMap.put(timeLimit, timesCounterMap.get(timeLimit) + 1);
        return timesCounterMap;
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

        // create game date column
        TableColumn<TableInfoContainer, String> dominantHandCol = new TableColumn<>("dominantHand");
        dominantHandCol.setCellValueFactory(new PropertyValueFactory<>("dominantHand"));
        dominantHandCol.setText("יד דומיננטית");
        this.resultsTable.getColumns().add(dominantHandCol);

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
                    .setNumOfRecognizedButtons(String.valueOf(g.getNumOfRecognizedButtons())).setDominantHand(g.getDominantHand());
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
