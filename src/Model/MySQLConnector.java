package Model;

import Resources.*;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class MySQLConnector {
    // members
    private Connection conn;
    private String host, port, user, password, schema;
    private Statement statement;
    private ResultSet resultSet;

    private String[] patientColumns = {"patient_id", "patient_name", "patient_gender", "dominant_hand", "patient_age"};
    private String[] gameColumns = {"game_id", "game_type", "num_recognized_buttons", "game_date", "time_limit"};
    private String[] shapesColumns = {"game_id", "arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private String[] texturesColumns = {"game_id", "four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};
    private String[] patient_game = {"patient_id", "game_id"};

    /****
     * Constructor
     */
    public MySQLConnector() {
        this.conn = null;
    }

    /**
     *
     * @return true if the connection was successfully set
     */
    public boolean openConnection() {

        // creating the connection.
        parseInfo();

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.schema
                    + "?useUnicode=true&characterEncoding=ISO8859_8&useLegacyDatetimeCode=false&serverTimezone=UTC", this.user, this.password);
        } catch (SQLException e) {
            System.out.println("Unable to connect - " + e.getMessage());
            this.conn = null;
            return false;
        }
        System.out.println("Connected!");
        return true;
    }


    /**
     * close the connection
     */
    public void closeConnection() {
        // closing the connection
        try {
            this.conn.close();
        } catch (SQLException e) {
            System.out.println("Unable to close the connection - " + e.getMessage());
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


    public void getPatientInfo(String patientID) {
        try {
            // create the sql query
            this.statement = this.conn.createStatement();
            String command = "Select * from patient where patient_id=\"" + patientID + "\";";
            // execute the query
            this.resultSet = this.statement.executeQuery(command);
            while(this.resultSet.next()) {

            }
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient info - " + e.getMessage());
        }
    }

    public void getResultsByPatient(String p_id, String[] tables) {
        try {
            // create sql query
            this.statement = this.conn.createStatement();
            for (String table : tables) {
                String command = String.format("Select * from %s, patient_game" +
                        " where patient_game.patient_id=\"%s\" and patient_game.game_id=%s.game_id;", table, p_id, table);
                this.resultSet = this.statement.executeQuery(command);
            }
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient results - " + e.getMessage());
        }
    }

    public void insertNewPatient(PatientContainer patientInfo) {
        String p_id = patientInfo.getPatientID(), p_name = patientInfo.getPatientName(), p_age = patientInfo.getPatientAge();
        char p_hand = patientInfo.getPatientDominantHand(), p_gender = patientInfo.getPatientGender();
        try {
            // create the sql query
            this.statement = this.conn.createStatement();
            String command = String.format("INSERT INTO patient (patient_id, patient_name, patient_gender, dominant_hand, patient_age)" +
                    " VALUES (\"%s\", \"%s\", \"%c\", \"%c\", \"%s\")", p_id, p_name, p_gender, p_hand, p_age);
            // execute the query
            this.resultSet = this.statement.executeQuery(command);
            this.conn.commit();
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient info - " + e.getMessage());
        }
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
