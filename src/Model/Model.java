package Model;

import Resources.GameContainer;
import Resources.PatientContainer;

import java.sql.SQLException;
import java.util.ArrayList;

/******
 * Model interface.
 * Responsible on the connection between the GUI and the DB.
 */
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

    /*****
     * Request information about a patient given his/her ID from the DB.
     * @param id the patient ID.
     * @return a Patient container with all patient information.
     */
    PatientContainer getData(String id);

    /*****
     * Request information about a patient games given his/her ID from the DB.
     * @param id the patient ID.
     * @param gameQueries instance of game queries.
     * @return a list of Game container with all patient games information.
     */
    ArrayList<GameContainer> getData(String id, GameQueries gameQueries);

    /****
     * Given a patient container - insert all the details into the DB.
     * @param patientContainer the given patient container.
     */
    void insertNewPatient(PatientContainer patientContainer);

    /******
     * Given patient container and game container - insert them into the DB.
     * @param patientContainer the given patient container.
     * @param gameContainer the given game container.
     */
    void insertNewGame(PatientContainer patientContainer, GameContainer gameContainer);

    /*****
     * returns the number of games
     * @param command get all games command
     * @return the number of games
     */
    int getGamesNumber(String command);

    /*****
     * returns all the IDs of the patients.
     * @return an array with all the IDs of the patients.
     */
    String[] getAllPatientsIDs();
}
