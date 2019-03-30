package Model;

import Resources.GameContainer;
import Resources.PatientContainer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Model {

    /**
     *
     * Closes the current connection to the DB
     * @throws SQLException thrown if unable to connect to the DB
     */
    void closeConnection() throws SQLException;

    /**
     *
     * Opens a connection to the DB.
     * @throws SQLException thrown if unable to disconnect to the DB
     */
    void openConnection() throws SQLException;

    PatientContainer getData(String id);

    ArrayList<GameContainer> getData(String id, GameQueries gameQueries);

    void insertNewPatient(PatientContainer patientContainer);

    void insertNewGame(PatientContainer patientContainer, GameContainer gameContainer);

    /*****
     * returns the number of games
     * @param command get all games command
     * @return the number of games
     */
    int getGamesNumber(String command);
}
