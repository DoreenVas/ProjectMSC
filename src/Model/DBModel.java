package Model;

import Resources.*;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class DBModel implements Model{
    // members
    private Connection conn;
    private String host, port, user, password, schema;
    private Statement statement;
    private ResultSet resultSet;


    private String[] gameColumns = {"game_id", "game_type", "num_recognized_buttons", "game_date", "time_limit"};
    private String[] shapesColumns = {"game_id", "arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private String[] texturesColumns = {"game_id", "four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};
    private String[] patient_game = {"patient_id", "game_id"};

    /****
     * Constructor
     */
    public DBModel() {
        this.conn = null;
    }

    /**
     *
     * Opens a connection to the DB.
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
     *
     * Closes the connection to the DB.
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
        String configFilePath = "projectmsc/src/Model/config";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
            // read the info from the config file
            row = reader.readLine();
            while(row != null) {

                info = row.replace(" ","").split("=");
                switch(info[0]) {
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
            return new PatientContainer(p_name, p_id, p_age, p_gender, dominant_hand);
        }
    }

    @Override
    public GameContainer getData(String id, GameQueries gameQueries) {
        return null;
    }

    public void insertNewPatient(PatientContainer patientInfo) {
        PatientQueries.getInstance(statement).insertNewPatient(patientInfo);
    }

    public void insertNewGame(PatientContainer patientContainer, GameContainer gameContainer) {
        String game_type = gameContainer.getGameType();
        int timeLimit = gameContainer.getTimeLimit(), numOfRecognizedButtons = gameContainer.getNumOfRecginizedButtons();
        int gameCount;
        try {
            // create the sql query
            this.statement = this.conn.createStatement();
            String command = String.format("insert into game (game_type, num_recognized_buttons, game_date, time_limit)" +
                    "values (\"%s\", %d, now(), %d);", game_type, numOfRecognizedButtons, timeLimit);
            // execute the query
            this.resultSet = this.statement.executeQuery(command);
            // count the number of games
            command = "SELECT count(game_id) from game;";
            this.resultSet = this.statement.executeQuery(command);
            gameCount = this.resultSet.getInt(1);

            insertNewGameResults(patientContainer, gameContainer, gameCount);
            this.conn.commit();
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in insert new game - " + e.getMessage());
        }
    }

    private void insertNewGameResults(PatientContainer patientContainer, GameContainer gameContainer, int game_id) {
        try {
            // create the sql query
            this.statement = this.conn.createStatement();
            // insert the game and the patient to patient_game table
            String command = String.format("insert into patient_game (patient_id, game_id) values (\"%s\", %d);"
                    , patientContainer.getPatientID(), game_id);
            // execute the query
            this.resultSet = this.statement.executeQuery(command);
            // check what type of game it is, and insert to the corresponding tables
            switch (gameContainer.getGameType()) {
                case "shapes":
                    insertResultsIntoTables(game_id, gameContainer, "shapes");
                    break;
                case "textures":
                    insertResultsIntoTables(game_id, gameContainer, "textures");
                    break;
                case "both":
                    insertResultsIntoTables(game_id, gameContainer, "shapes");
                    insertResultsIntoTables(game_id, gameContainer, "textures");
                    break;
            }
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in insert new game - " + e.getMessage());
        }
    }

    private void insertResultsIntoTables(int game_id, GameContainer gameContainer, String table) {
        StringBuilder command1, command2;
        command1 = new StringBuilder("insert into ").append(table).append(" (");
        command2 = new StringBuilder(" values(");

        HashMap<String, Double> shapesResults = gameContainer.getShapesReactionTime();
        try {
            // create the sql query
            this.statement = this.conn.createStatement();

            // insert the game and the patient to patient_game table
            // add te results for each shape
            for (String k : shapesResults.keySet()) {
                command1.append(k).append(", ");
                command2.append(shapesResults.get(k)).append(", ");
            }
            // add the game id
            command1 = command1.append("game_id").append(")");
            command2 = command2.append(game_id).append(");");
            command1.append(command2.toString());
            // execute the query
            this.resultSet = this.statement.executeQuery(command1.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
