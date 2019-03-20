package Model;

import Resources.GameContainer;
import Resources.PatientContainer;
import Resources.imageTypeEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * A class holding all of the artist table queries, also in charge of executing them.
 */
public class GameQueries {
    // members
    private static GameQueries ourInstance = new GameQueries();
    private static Statement myStatement;
    private static String[] allGameFields = {/*"artist.artist_id", */"artist.artist_name", "artist.hotness"};
    private static String[] shapesColumns = {"game_id", "arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private static String[] texturesColumns = {"game_id", "four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};
    private ResultSet resultSet;


    /****
     * Constructor of singleton
     * @param statement a statement
     * @return his instance of artistQueries
     */
    static GameQueries getInstance(Statement statement) {
        myStatement = statement;
        return ourInstance;
    }

    /******
     * insert a new game into the game table
     * @param patientContainer the info of the patient
     * @param gameContainer the results of the current game
     */
    public void insertNewGame(PatientContainer patientContainer, GameContainer gameContainer) {
        String game_type = gameContainer.getGameType();
        int timeLimit = gameContainer.getTimeLimit(), numOfRecognizedButtons = gameContainer.getNumOfRecginizedButtons();
        int gameCount;
        try {
            String command = String.format("insert into game (game_type, num_recognized_buttons, game_date, time_limit)" +
                    "values (\"%s\", %d, now(), %d)", game_type, numOfRecognizedButtons, timeLimit);
            // execute the query
            myStatement.execute(command);
            // count the number of games
            command = "SELECT count(game_id) from game";
            this.resultSet = myStatement.executeQuery(command);
            this.resultSet.next();
            gameCount = this.resultSet.getInt("count(game_id)");

            insertNewGameResults(patientContainer, gameContainer, gameCount);
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in insert new game - " + e.getMessage());
        }
    }

    /*****
     * insert the new game into patient_game table.
     * adds the connection between a patient name and a game id
     * @param patientContainer the patient info
     * @param gameContainer the info of the game
     * @param game_id the game id
     */
    private void insertNewGameResults(PatientContainer patientContainer, GameContainer gameContainer, int game_id) {
        try {
            // insert the game and the patient to patient_game table
            String command = String.format("insert into patient_game (patient_id, game_id) values (\"%s\", %d);"
                    , patientContainer.getPatientID(), game_id);
            // execute the query
            myStatement.execute(command);
            // check what type of game it is, and insert to the corresponding tables
            switch (gameContainer.getGameType()) {
                case "Shapes":
                    insertResultsIntoTables(game_id, gameContainer, "shapes");
                    break;
                case "Textures":
                    insertResultsIntoTables(game_id, gameContainer, "textures");
                    break;
                case "Both":
                    insertResultsIntoTables(game_id, gameContainer, "shapes");
                    insertResultsIntoTables(game_id, gameContainer, "textures");
                    break;
            }
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in insert new game - " + e.getMessage());
        }
    }

    /*****
     * insert the results to the given table (shapes/ textures)
     * @param game_id the game id
     * @param gameContainer the results of the game
     * @param table the table name
     */
    private void insertResultsIntoTables(int game_id, GameContainer gameContainer, String table) {
        StringBuilder command1, command2;
        command1 = new StringBuilder("insert into ").append(table).append(" (");
        command2 = new StringBuilder(" values(");


        try {
            // insert the game and the patient to patient_game table
            // add te results for each shape/texture
            switch (table) {
                case "shapes":
                    HashMap<String, Double> shapesResults = gameContainer.getShapesReactionTime();
                    for (String k : shapesResults.keySet()) {
                        command1.append(k.replace(".png", "")).append(", ");
                        command2.append(shapesResults.get(k)).append(", ");
                    }
                    break;
                case "textures":
                    HashMap<String, Double> texturesResults = gameContainer.getTexturesReactionTime();
                    for (String k : texturesResults.keySet()) {
                        command1.append(k.replace(".png", "")).append(", ");
                        command2.append(texturesResults.get(k)).append(", ");
                    }
                    break;
            }
            // add the game id
            command1 = command1.append("game_id").append(")");
            command2 = command2.append(game_id).append(");");
            command1.append(command2.toString());
            // execute the query
            myStatement.execute(command1.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*****
     * given an image returns the type of the image (shape or texture)
     * @param img the given image
     * @return the type of the image (as enum)
     */
    public static String getImageType(String img) {
        // check if the image is in the shapes list
        for (String s : shapesColumns) {
            if (s.equals(img)) {
                return imageTypeEnum.SHAPES.getType();
            }
        }
        // check if the image is in the textures list
        for (String s : texturesColumns) {
            if (s.equals(img)) {
                return imageTypeEnum.TEXTURES.getType();
            }
        }
        // default value
        return null;
    }
}