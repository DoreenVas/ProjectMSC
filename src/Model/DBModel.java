package Model;

import GUI.Alerter;
import Resources.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBModel implements Model {
    // members
    private Connection conn;
    private String host, port, user, password, schema;
    private Statement statement;


    private String[] gameColumns = {"game_id", "game_type", "num_recognized_buttons", "game_date", "time_limit"};
    private String[] patient_game = {"patient_id", "game_id"};

    /****
     * Constructor
     */
    public DBModel() {
        this.conn = null;
    }

    /**
     * Opens a connection to the DB.
     *
     * @throws DBConnectionException thrown if there was an error connecting to the DB
     */
    public void openConnection() throws DBConnectionException {
        // creating the connection.
        parseInfo();

        try {
            // start connection to DBConnection
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.schema
                    + "?useUnicode=true&characterEncoding=ISO8859_8&useLegacyDatetimeCode=false&serverTimezone=UTC", this.user, this.password);
            // create a statement
            this.statement = this.conn.createStatement();
        } catch (Exception e) {
            throw new DBConnectionException(AlertMessages.failedConnection(), e);
        }
    }

    /**
     * Closes the connection to the DB.
     *
     * @throws DBConnectionException thrown if there was an error disconnecting from the DB
     */
    public void closeConnection() throws DBConnectionException {
        try {
            // close resources
            this.statement.close();
            this.conn.close();
        } catch (Exception e) {
            throw new DBConnectionException(AlertMessages.failedDisconnection(), e);
        }
    }

    /*****
     * Parse the information of the configuration file, to connect the DB
     */
    private void parseInfo() {
        String row;
        String[] info;
        // Current working directory is ProjectMSC
        // the path to the config file
        String configFilePath = "src/Model/config";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
            // read the info from the config file
            row = reader.readLine();
            while (row != null) {

                info = row.replace(" ", "").split("=");
                switch (info[0]) {
                    case "host":
                        this.host = info[1];
                        break;
                    case "port":
                        this.port = info[1];
                        break;
                    case "schema":
                        this.schema = info[1];
                        break;
                    case "user":
                        this.user = info[1];
                        break;
                    case "password":
                        this.password = info[1];
                        break;
                }
                row = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open config file reader\n");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read from config file\n");
            e.printStackTrace();
        }
    }

    public PatientContainer getData(String id) {
        String[] info = PatientQueries.getInstance(this.statement).getPatientInfo(id);
        if (info[0].equals("")) {
            return null;
        } else {
            String[] splitInfo = info[0].split(",");
            String p_id = splitInfo[0];
            String p_name = splitInfo[1];
            String p_gender = splitInfo[2];
            String dominant_hand = splitInfo[3];
            String p_age = splitInfo[4];
            return PatientContainer.getInstance().setPatientAge(p_age).setPatientDominantHand(dominant_hand)
                    .setPatientGender(p_gender).setPatientID(p_id).setPatientName(p_name);
        }
    }

    @Override
    public ArrayList<GameContainer> getData(String id, GameQueries gameQueries) {
        String[] games = GameQueries.getInstance(this.statement).getPatientGames(id);
        // check if we received any games
        if (games[0].equals("")) {
            return null;
        }
        return parseGames(games);
    }

    /*****
     * The function gets an array of games and returns a list of game containers of those games
     * @param games an array of games
     * @return a list of game containers
     */
    private ArrayList<GameContainer> parseGames(String[] games) {
        String[] game;
        String gameType, gameDate, dominantHand;
        int gameId, numOfRecognizedButtons, timeLimit;
        HashMap<String, Double> shapesReactionTimes, texturesReactionTimes;
        ArrayList<GameContainer> gamesList = new ArrayList<>();
        // go over the games info
        for (String g : games) {
            // parse the game info into fields
            game = g.split(",");
            gameId = Integer.parseInt(game[0]);
            gameType = game[1];
            numOfRecognizedButtons = Integer.parseInt(game[2]);
            gameDate = game[3];
            timeLimit = Integer.parseInt(game[4]);
            dominantHand = game[5];
            // if game type is both - add shapes and textures maps
            if (gameType.equals("Both")) {
                shapesReactionTimes = GameQueries.getInstance(this.statement).getTimesOfGame(gameId, "Shapes");
                texturesReactionTimes = GameQueries.getInstance(this.statement).getTimesOfGame(gameId, "Textures");
            } else {
                // add only shapes map
                if (gameType.equals("Shapes")) {
                    shapesReactionTimes = GameQueries.getInstance(this.statement).getTimesOfGame(gameId, gameType);
                    texturesReactionTimes = new HashMap<>();
                } else {
                    // add only textures map
                    texturesReactionTimes = GameQueries.getInstance(this.statement).getTimesOfGame(gameId, gameType);
                    shapesReactionTimes = new HashMap<>();
                }
            }
            // create the game container
            GameContainer gameContainer = new GameContainer(shapesReactionTimes,
                    texturesReactionTimes, numOfRecognizedButtons, timeLimit, gameType, dominantHand);
            gameContainer.setGameDate(gameDate);
            gamesList.add(gameContainer);
        }
        return gamesList;
    }

    @Override
    public void insertNewPatient(PatientContainer patientInfo) {
        PatientQueries.getInstance(this.statement).insertNewPatient(patientInfo);
    }

    @Override
    public void insertNewGame(PatientContainer patientContainer, GameContainer gameContainer) {
        GameQueries.getInstance(this.statement).insertNewGame(patientContainer, gameContainer);
    }


    /**
     * Attempts to set the connection back to auto-commit, ignoring errors.
     */
    private void safelySetAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (Exception e) {
        }
    }
}
