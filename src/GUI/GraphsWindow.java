package GUI;

import Model.Connection;
import javafx.scene.Cursor;
import Resources.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;
import javafx.scene.control.Label;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/******
 * Graphs window class.
 * This window is the personal zone of the user.
 * In this window we view the details of the patient and all the reaction time
 * for each game that was played by the patient.
 * We also view a line chart of avg reaction time of all games of the patient.
 */
public class GraphsWindow extends BasicWindow implements Initializable{
    // members
    @FXML
    private ImageView back;
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

    @FXML
    private Label infoPatientName;
    @FXML
    private Label patientGender;
    @FXML
    private Label patientDateOfBirth;
    @FXML
    private Label patientDominantHand;
    @FXML
    private Label patientGamesPlayed;

    private HashMap<String, XYChart.Series> shapesSeries;
    private HashMap<String, XYChart.Series> texturesSeries;

    private String previousScene;
    private double window_height;
    private double window_width;

    private static String[] shapesColumns = {"arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private static String[] texturesColumns = {"four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};

    /***
     * saving the name of the previous window (menu or admin).
     * @param prevScene the name of the previous window.
     */
    public void setPreviousScene(String prevScene){
        this.previousScene = prevScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // show patient name
        super.initialize(location, resources);
        int index[] = null;
        // add click option to table row, to show bar chart
        this.resultsTable.setRowFactory(tv -> {
            TableRow<TableInfoContainer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    TableInfoContainer clickedRow = row.getItem();
                    showPersonalBarChart(clickedRow);
                }
            });
            return row ;
        });

        // initialize maps
        this.shapesSeries = new HashMap<>();
        this.texturesSeries = new HashMap<>();
        this.shapesLineChart.setCursor(Cursor.CROSSHAIR);
        this.texturesLineChart.setCursor(Cursor.CROSSHAIR);
        // initialize patient info
        initializePatientInfo();
        index = loadChartData();
        // check if we received any games
        if (index == null) {
            return;
        }
        this.shapes_game_id.setAutoRanging(false);
        this.shapes_game_id.setLowerBound(0);
        this.shapes_game_id.setUpperBound(index[0]);
        this.shapes_game_id.setTickUnit(1);

        this.shapes_avgReactionTime.setAutoRanging(false);
        this.shapes_avgReactionTime.setLowerBound(0);
        this.shapes_avgReactionTime.setUpperBound(70);
        this.shapes_avgReactionTime.setTickUnit(10);

        this.textures_game_id.setAutoRanging(false);
        this.textures_game_id.setLowerBound(0);
        this.textures_game_id.setUpperBound(index[1]);
        this.textures_game_id.setTickUnit(1);

        this.textures_avgReactionTime.setAutoRanging(false);
        this.textures_avgReactionTime.setLowerBound(0);
        this.textures_avgReactionTime.setUpperBound(70);
        this.textures_avgReactionTime.setTickUnit(10);
    }

    /******
     * The function initializes the information of the patient to display on the
     * first tab of the graphs window.
     */
    private void initializePatientInfo() {
        // initialize patient info
        PatientContainer p = PatientContainer.getInstance();
        this.infoPatientName.setText(p.getPatientName());
        this.patientDateOfBirth.setText(p.getPatientAge());
        this.patientDominantHand.setText(p.getPatientDominantHand());
        this.patientGender.setText(p.getPatientGender());

        // get number of games
        String command = "SELECT count(*) FROM patient_game where patient_id=\"" + p.getPatientID()+"\"";
        try {
            Connection conn = Connection.getInstance();
            int gamesNum = conn.getGamesNumber(command);
            this.patientGamesPlayed.setText(String.valueOf(gamesNum));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /******
     * The function loads all the games of the current patient and store them in
     * series to add the results table.
     *
     * @return The function returns the number of values in the biggest category of
     * both the shapes and the textures series (an array of int).
     */
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

    /********
     *
     * @param g the container of the current game.
     * @param timesCounterMap a map that counts the number of values in each category (the categories are
     *                        the time limits [30/45/60 sec]. We count how many games where played on each
     *                        time limit).
     * @param gType the game type.
     * @return a map of each category and number of games that where played on each category.
     */
    private HashMap<String, Integer> insertToSeries(GameContainer g,
                                                   HashMap<String, Integer> timesCounterMap, String gType) {
        String timeLimit = String.valueOf(g.getTimeLimit());
        double avg;
        String sec = "";
        // set the seconds word according to the app language
        switch (MainWindow.language) {
            case "Hebrew":
                sec = " שניות";
                break;
            case "English":
                sec = " seconds";
                break;
        }

        // check the type of game
        switch (gType) {
            case "Shapes":
                // check if the category is already in the map
                if (!timesCounterMap.containsKey(timeLimit)) {
                    // add a new serie, and count it
                    timesCounterMap.put(timeLimit, 1);
                    this.shapesSeries.put(timeLimit, new XYChart.Series());
                    // set the category title
                    this.shapesSeries.get(timeLimit).setName(g.getTimeLimit() + sec);
                }
                avg = calculateAvgReactionTimeForGraph(g.getShapesReactionTime());
                XYChart.Data<Integer, Double> dataShapes = new XYChart.Data<>(timesCounterMap.get(timeLimit), avg);
                // add hover node
                dataShapes.setNode(new HoveredThresholdNode((double)Math.round(avg * 100) / 100));
                this.shapesSeries.get(timeLimit).getData().add(dataShapes);
                break;
            case "Textures":
                // check if the category is already in the map
                if (!timesCounterMap.containsKey(timeLimit)) {
                    // add a new serie, and count it
                    timesCounterMap.put(timeLimit, 1);
                    this.texturesSeries.put(timeLimit, new XYChart.Series());
                    // set the category title
                    this.texturesSeries.get(timeLimit).setName(g.getTimeLimit() +sec);
                }
                avg = calculateAvgReactionTimeForGraph(g.getTexturesReactionTime());
                XYChart.Data<Integer, Double> dataTextures = new XYChart.Data<>(timesCounterMap.get(timeLimit), avg);
                // add hover node
                dataTextures.setNode(new HoveredThresholdNode((double)Math.round(avg * 100) / 100));
                this.texturesSeries.get(timeLimit).getData().add(dataTextures);
                break;
        }
        // add 1 to the counter of the time limit
        timesCounterMap.put(timeLimit, timesCounterMap.get(timeLimit) + 1);
        return timesCounterMap;
    }

    /****
     * the function returns the max value in a map.
     * @param map a map of number of games in each time limit.
     * @return a max integer in the map.
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

    /*****
     * the function calculates the average reaction time of a game
     * @param map a map of all reaction times
     * @return the average reaction time of the game
     */
    private double calculateAvgReactionTimeForGraph(HashMap<String, Double> map) {
        double avg = 0;
        for (Double d : map.values()) {
            avg += d;
        }
        avg = avg / map.values().size();
        return avg;
    }

    /******
     * The function creates the table, fill the titles and columns according to data it receives.
     * @param gamesList A list of all games data.
     */
    private void createTable(ArrayList<GameContainer> gamesList) {
        String game_type_txt = "";
        String time_limit_txt = "";
        String num_recognized_txt = "";
        String game_date_txt = "";
        String dominant_hand_txt = "";
        // set the titles of the table according to the current language of the app.
        switch (MainWindow.language) {
            case "Hebrew":
                game_type_txt = "סוג משחק";
                time_limit_txt = "מגבלת הזמן";
                num_recognized_txt = "מספר התמונות שזוהו";
                game_date_txt = "תאריך המשחק";
                dominant_hand_txt = "יד דומיננטית";
                break;
            case "English":
                game_type_txt = "Game type";
                time_limit_txt = "Time Limit";
                num_recognized_txt = "Number of recognized pictures";
                game_date_txt = "Game date";
                dominant_hand_txt = "Dominant hand";
                break;
        }

        // create game type column
        TableColumn<TableInfoContainer, String> gameTypeCol = new TableColumn<>("gameType");
        gameTypeCol.setCellValueFactory(new PropertyValueFactory<>("gameType"));
        gameTypeCol.setText(game_type_txt);
        gameTypeCol.setMinWidth(game_type_txt.length());
        this.resultsTable.getSortOrder().add(gameTypeCol);
        this.resultsTable.getColumns().add(gameTypeCol);

        // create time limit column
        TableColumn<TableInfoContainer, String> timeLimitCol = new TableColumn<>("timeLimit");
        timeLimitCol.setCellValueFactory(new PropertyValueFactory<>("timeLimit"));
        timeLimitCol.setText(time_limit_txt);
        timeLimitCol.setMinWidth(time_limit_txt.length());
        this.resultsTable.getSortOrder().add(timeLimitCol);
        this.resultsTable.getColumns().add(timeLimitCol);

        // create number of recognized images column
        TableColumn<TableInfoContainer, String> recognizedCol = new TableColumn<>("numOfRecognizedButtons");
        recognizedCol.setCellValueFactory(new PropertyValueFactory<>("numOfRecognizedButtons"));
        recognizedCol.setText(num_recognized_txt);
        recognizedCol.setMinWidth(num_recognized_txt.length());
        this.resultsTable.getSortOrder().add(recognizedCol);
        this.resultsTable.getColumns().add(recognizedCol);

        // create game date column
        TableColumn<TableInfoContainer, String> dateCol = new TableColumn<>("gameDate");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("gameDate"));
        dateCol.setText(game_date_txt);
        dateCol.setMinWidth(game_date_txt.length());
        this.resultsTable.getSortOrder().add(dateCol);
        this.resultsTable.getColumns().add(dateCol);

        // create game date column
        TableColumn<TableInfoContainer, String> dominantHandCol = new TableColumn<>("dominantHand");
        dominantHandCol.setCellValueFactory(new PropertyValueFactory<>("dominantHand"));
        dominantHandCol.setText(dominant_hand_txt);
        dominantHandCol.setMinWidth(dominant_hand_txt.length());
        this.resultsTable.getSortOrder().add(dominantHandCol);
        this.resultsTable.getColumns().add(dominantHandCol);

        // create column for every image
        for (String s : shapesColumns) {
            TableColumn<TableInfoContainer, String> shapesColumn = new TableColumn<>(s);
            shapesColumn.setCellValueFactory(new PropertyValueFactory<>(s));
            shapesColumn.setMinWidth(s.length());
            this.resultsTable.getColumns().add(shapesColumn);
        }
        for (String s : texturesColumns) {
            TableColumn<TableInfoContainer, String> texturesColumn = new TableColumn<>(s);
            texturesColumn.setCellValueFactory(new PropertyValueFactory<>(s));
            texturesColumn.setMinWidth(s.length());
            this.resultsTable.getColumns().add(texturesColumn);
        }

        // add columns to the table
        this.resultsTable.getItems().addAll(getGames(gamesList));
    }

    /****
     * the function makes an observable list of table info container (for the table view)
     * @param list a list of game containers
     * @return an observable list of table info containers
     */
    private ObservableList<TableInfoContainer> getGames(ArrayList<GameContainer> list) {
        ObservableList<TableInfoContainer> games = FXCollections.observableArrayList();
        // go over the games
        for (GameContainer g : list) {
            TableInfoContainer t = new TableInfoContainer();
            // set the game info
            t.setGameType(g.getGameType()).setTimeLimit(String.valueOf(g.getTimeLimit())).setGameDate(g.getGameDate())
                    .setNumOfRecognizedButtons(String.valueOf(g.getNumOfRecognizedButtons())).setDominantHand(dominantHandSwitchLanguage(g.getDominantHand()));
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
     * The function receives the value of dominant hand and returns it
     * according to the current language of the app.
     * @param dominantHand the value of dominant hand.
     * @return the translation according to the current language of the app.
     */
    private String dominantHandSwitchLanguage(String dominantHand) {
        if (MainWindow.language.equals("Hebrew")) {
            switch (dominantHand) {
                case "Yes":
                    return "כן";
                case "No":
                    return "לא";
                default:
                    return dominantHand;
            }
        } else {
            switch (dominantHand) {
                case "כן":
                    return "Yes";
                case "לא":
                    return "No";
                default:
                    return dominantHand;
            }
        }
    }

    /*****
     * insert the reaction time to the tableInfoContainer
     * @param t the tableInfoContainer.
     * @param g the gameContainer of the current game.
     * @param field the current image.
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
    }

    /*******
     * The function opens a file chooser, so the user can save the table and line charts
     * into an excel file.
     * The function calls a python script (ExcelWriter.py) in order to create the excel file.
     * The script is created on the desktop and removed after the save is made.
     */
    @FXML
    private void savePatientInfo() {
        Stage secondStage = new Stage();
        // open fileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialDirectory(javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory());

        // add file extensions
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX files (*.xlsx)", "*.xlsx")
        );

        File file = fileChooser.showSaveDialog(secondStage);
        File tempFile = null;
        if (file != null) {
            try {
                // get the info for the excel file
                String tableInfo = getTableInfoForExcel();
                String shapesInfo = getLineChartInfo(this.shapesSeries);
                String texturesInfo = getLineChartInfo(this.texturesSeries);

                // run the script
                // get the path to the script
                URL excelWriterPath = this.getClass().getResource("/Model/ExcelWriter.py");
                // read the script
                byte[] buffer = new byte[excelWriterPath.openStream().available()];
                excelWriterPath.openStream().read(buffer);
                // copy the python script to the desktop
                String tempFilePath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "/ExcelWriter.py";
                tempFile = new File(tempFilePath);
                OutputStream outStream = new FileOutputStream(tempFile);
                outStream.write(buffer);
                outStream.close();
                // run the script from the desktop
                Process p = Runtime.getRuntime().exec(new String[]{"python", tempFile.getPath(), file.getAbsolutePath(), tableInfo, shapesInfo, texturesInfo});
                // get a return output from the program
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = reader.readLine();
                // check if open failed
                if (line != null && line.equals("file not saved")) {
                    Alerter.showAlert(AlertMessages.errorFileUsedByAnotherProcess(), Alert.AlertType.WARNING);
                }
                reader.close();
                // delete the script
                tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }

    /*****
     * the function builds a string of all the table info and returns it
     * each row separated by "<" and each value in the row by "!"
     * @return a string of all the table info
     */
    private String getTableInfoForExcel() {
        // initialize the argument string
        StringBuilder list = new StringBuilder("");
        // get the titles a string
        TableInfoContainer temp = this.resultsTable.getItems().get(0);
        StringBuilder titles = new StringBuilder("<");
        for (String title : temp.getTitles()) {
            titles.append(title);
            titles.append("!");
        }
        // add the titles
        list.append(titles);
        // for each row in the table, add all info to the string
        for (TableInfoContainer t : this.resultsTable.getItems()) {
            // separate each row with "<", and each value by "!"
            StringBuilder row = new StringBuilder("<");
            ArrayList<String> values = t.getValues();
            for (String s : values) {
                row.append(s);
                row.append("!");
            }
            list.append(row.toString());
        }
        return list.toString();
    }

    /*****
     * the function builds a string of all the charts info and returns it
     * each row separated by "<" and each value in the row by "!"
     * @return a string of all the chart info
     */
    private String getLineChartInfo(HashMap<String, XYChart.Series> series) {
        StringBuilder str = new StringBuilder("");
        for (XYChart.Series serie : series.values()) {
            str.append("<");
            str.append(serie.getName().replace(" שניות", "")).append("!");
            for (Object s : serie.getData()) {
                str.append(s);
                str.append("!");
            }
        }
        return str.toString();
    }

    /*****
     * Double click on a row in the table opens a new window with a bar chart
     * in it, that represents the reaction times of the specific game.
     * @param tableInfoContainer the row info from the table.
     */
    private void showPersonalBarChart(TableInfoContainer tableInfoContainer) {
        // create the window
        Stage secondStage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        String gameType = tableInfoContainer.getGameType();
        // create the bar chart
        BarChart barChart = createBarChart(gameType, gameType, "Reaction Time", tableInfoContainer);
        anchorPane.getChildren().add(barChart);
        Scene scene = new Scene(anchorPane, 600, 600);
        scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
        // bind the bar chart to the size of the window
        barChart.minWidthProperty().bind(anchorPane.widthProperty());
        barChart.minHeightProperty().bind(anchorPane.heightProperty().subtract(20));
        anchorPane.minWidthProperty().bind(scene.widthProperty());
        anchorPane.minHeightProperty().bind(scene.heightProperty().subtract(20));
        // show the window
        secondStage.setScene(scene);
        secondStage.show();
    }

    /******
     * the function returns a bar chart of reaction times according to the game type
     * @param chartName the name of the chart
     * @param xAxisName the x axis name
     * @param yAxisName the y axis name
     * @param tableInfoContainer the information of the current game
     * @return a bar chart
     */
    private BarChart<String, Number> createBarChart(String chartName, String xAxisName, String yAxisName, TableInfoContainer tableInfoContainer) {
        CategoryAxis xAxis = new CategoryAxis();
        if (MainWindow.language.equals("Hebrew")) {
            xAxis.setLabel(xAxisName);
        } else {
            xAxis.setLabel(toEnglish(xAxisName));
        }

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisName);

        //Creating the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        if (MainWindow.language.equals("Hebrew")) {
            barChart.setTitle(chartName);
        } else {
            barChart.setTitle(toEnglish(chartName));
        }
        // add the data to the corresponding chart.
        switch (chartName) {
            case "צורות":
            case "Shapes":
                // get a list of all shapes images titles
                xAxis.setCategories(setShapesCategories());
                // add the data
                barChart.getData().addAll(shapesBarSeries(tableInfoContainer));
                break;
            case "מרקמים":
            case "Textures":
                // get a list of all textures images titles
                xAxis.setCategories(setTexturesCategories());
                // add the data
                barChart.getData().addAll(texturesBarSeries(tableInfoContainer));
                break;
            case "משולב":
            case "Both":
                // get a list of all images titles
                ObservableList<String> list = setShapesCategories();
                list.addAll(setTexturesCategories());
                xAxis.setCategories(list);
                // add the data
                barChart.getData().addAll(shapesBarSeries(tableInfoContainer));
                barChart.getData().addAll(texturesBarSeries(tableInfoContainer));
                break;
        }
        barChart.getXAxis().setTickLabelRotation(45);
        barChart.setStyle("-fx-font-size:13px;");
        return barChart;
    }

    /****
     * the function receives the chart name in hebrew and converts it to english.
     * @param chartName the chart name in hebrew.
     * @return the chart name in english.
     */
    private String toEnglish(String chartName) {
        switch (chartName) {
            case "צורות":
                return "Shapes";
            case "מרקמים":
                return "Textures";
            case "משולב":
                return "Both";
            default:
                return chartName;
        }
    }

    /*****
     * the function returns a list of shapes titles
     * @return an observable list of the shapes images' titles for the bar chart.
     */
    private ObservableList<String> setShapesCategories() {
        ObservableList<String> shapes = FXCollections.observableArrayList();
        for(String s : shapesColumns) {
            shapes.add(s);
        }
        return shapes;
    }

    /*****
     * the function returns a list of textures titles
     * @return an observable list of the texture images' titles for the bar chart.
     */
    private ObservableList<String> setTexturesCategories() {
        ObservableList<String> textures = FXCollections.observableArrayList();
        for(String s : texturesColumns) {
            textures.add(s);
        }
        return textures;
    }

    /****
     * the function returns a serie of shapes and reaction times
     * @param tableInfoContainer the info of the current game from the table
     * @return a series of shapes values for the bar chart.
     */
    private XYChart.Series shapesBarSeries(TableInfoContainer tableInfoContainer) {
        String name = "";
        switch (MainWindow.language) {
            case "Hebrew":
                name = "צורות";
                break;
            case "English":
                name = "Shapes";
                break;
        }
        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);
        series.getData().add(new XYChart.Data<>("arrow", Double.parseDouble(tableInfoContainer.getArrow())));
        series.getData().add(new XYChart.Data<>("rectangle", Double.parseDouble(tableInfoContainer.getRectangle())));
        series.getData().add(new XYChart.Data<>("diamond", Double.parseDouble(tableInfoContainer.getDiamond())));
        series.getData().add(new XYChart.Data<>("triangle", Double.parseDouble(tableInfoContainer.getTriangle())));
        series.getData().add(new XYChart.Data<>("heart", Double.parseDouble(tableInfoContainer.getHeart())));
        series.getData().add(new XYChart.Data<>("flower", Double.parseDouble(tableInfoContainer.getFlower())));
        series.getData().add(new XYChart.Data<>("hexagon", Double.parseDouble(tableInfoContainer.getHexagon())));
        series.getData().add(new XYChart.Data<>("moon", Double.parseDouble(tableInfoContainer.getPlus())));
        series.getData().add(new XYChart.Data<>("oval", Double.parseDouble(tableInfoContainer.getPie())));
        series.getData().add(new XYChart.Data<>("two_triangles", Double.parseDouble(tableInfoContainer.getTwo_triangles())));
        series.getData().add(new XYChart.Data<>("circle", Double.parseDouble(tableInfoContainer.getCircle())));
        series.getData().add(new XYChart.Data<>("star", Double.parseDouble(tableInfoContainer.getStar())));
        series.getData().add(new XYChart.Data<>("pie", Double.parseDouble(tableInfoContainer.getPie())));
        series.getData().add(new XYChart.Data<>("plus", Double.parseDouble(tableInfoContainer.getPlus())));
        return series;
    }

    /****
     * the function returns a serie of textures and reaction times
     * @param tableInfoContainer the info of the current game from the table
     * @return a serie of textures values for the bar chart.
     */
    private XYChart.Series texturesBarSeries(TableInfoContainer tableInfoContainer) {
        String name = "";
        switch (MainWindow.language) {
            case "Hebrew":
                name = "מרקמים";
                break;
            case "English":
                name = "Textures";
                break;
        }
        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);
        series.getData().add(new XYChart.Data<>("four_dots", Double.parseDouble(tableInfoContainer.getFour_dots())));
        series.getData().add(new XYChart.Data<>("waves", Double.parseDouble(tableInfoContainer.getWaves())));
        series.getData().add(new XYChart.Data<>("arrow_head", Double.parseDouble(tableInfoContainer.getArrow_head())));
        series.getData().add(new XYChart.Data<>("strips", Double.parseDouble(tableInfoContainer.getStrips())));
        series.getData().add(new XYChart.Data<>("happy_smiley", Double.parseDouble(tableInfoContainer.getHappy_smiley())));
        series.getData().add(new XYChart.Data<>("spikes", Double.parseDouble(tableInfoContainer.getSpikes())));
        series.getData().add(new XYChart.Data<>("dollar", Double.parseDouble(tableInfoContainer.getDollar())));
        series.getData().add(new XYChart.Data<>("net", Double.parseDouble(tableInfoContainer.getNet())));
        series.getData().add(new XYChart.Data<>("note", Double.parseDouble(tableInfoContainer.getNote())));
        series.getData().add(new XYChart.Data<>("arcs", Double.parseDouble(tableInfoContainer.getArcs())));
        series.getData().add(new XYChart.Data<>("monitor", Double.parseDouble(tableInfoContainer.getMonitor())));
        series.getData().add(new XYChart.Data<>("sad_smiley", Double.parseDouble(tableInfoContainer.getSad_smiley())));
        series.getData().add(new XYChart.Data<>("strudel", Double.parseDouble(tableInfoContainer.getStrudel())));
        series.getData().add(new XYChart.Data<>("four_bubbles", Double.parseDouble(tableInfoContainer.getFour_bubbles())));
        series.getData().add(new XYChart.Data<>("squares", Double.parseDouble(tableInfoContainer.getSquares())));
        series.getData().add(new XYChart.Data<>("spiral", Double.parseDouble(tableInfoContainer.getSpiral())));
        return series;
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
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
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
