package Resources;

import GUI.MainWindow;
import Model.Connection;
import Resources.GameContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PieChartBuilder {
    // members

    /****
     * the function creates a pie chart of error/success rate of specific game type
     * @param gameType the game type
     * @return a pie chart
     */
    public static PieChart createChart(String gameType) {
        String[] ids = getPatientsIDs();
        ArrayList<Double> gameSuccesses = analyzeGamesForPatients(ids, gameType);
        ObservableList<PieChart.Data> pieChartData = getPieChartData(gameSuccesses);
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(gameType);
        return pieChart;
    }

    /****
     * the function returns all the ids of the patients as a string array
     * @return String[]
     */
    private static String[] getPatientsIDs() {
        String[] ids = null;
        try {
            Connection connection = Connection.getInstance();
            ids = connection.getAllPatientsIDs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /*****
     * the function calculates success rate for each patient and return a list of successes
     * @param ids an array of patient ids
     * @param gameType the wanted game type
     * @return a list of success rate
     */
    private static ArrayList<Double> analyzeGamesForPatients(String[] ids, String gameType) {
        ArrayList<GameContainer> g;
        ArrayList<Double> personalSuccessRate = new ArrayList<>();
        try {
            Connection connection = Connection.getInstance();
            for (String id : ids) {
                g = connection.getGames(id);
                if (g == null) {
                    continue;
                }
                double success = getGamesSuccessRate(g, gameType);
                personalSuccessRate.add(success);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personalSuccessRate;
    }

    /*****
     * the function returns the success rate of all games according to game type
     * @param games all games of type gameType
     * @param gameType the gameType
     * @return a success rate
     */
    private static double getGamesSuccessRate(ArrayList<GameContainer> games, String gameType) {
        double success = 0;
        int gamesCounter = 0;
        for (GameContainer g : games) {
            if (g.getGameType().equals(gameType)) {
                success += g.getNumOfRecognizedButtons();
                gamesCounter++;
            }
        }
        switch (gameType) {
            case "Shapes":
                success /= (14 * gamesCounter);
                break;
            case "Textures":
                success /= (16 * gamesCounter);
                break;
            case "Both":
                success /= (30 * gamesCounter);
                break;
        }
        return success;
    }

    /*****
     * the function returns the pie chart data
     * @param successes the success rate of all patients
     * @return pie chart data
     */
    private static ObservableList<PieChart.Data> getPieChartData(ArrayList<Double> successes) {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
        double sum = 0;
        for (Double d : successes) {
            sum += d;
        }
        sum /= successes.size();
        if (MainWindow.language.equals("Hebrew")) {
            list.addAll(new PieChart.Data("הצלחה", sum), new PieChart.Data("כשלון", 1 - sum));
        } else {
            list.addAll(new PieChart.Data("Success", sum), new PieChart.Data("Failure", 1 - sum));
        }
        return list;
    }


}

