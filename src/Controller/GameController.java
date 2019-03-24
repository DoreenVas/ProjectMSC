package Controller;

import Model.GameQueries;
import Resources.GameContainer;
import Resources.PatientContainer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/***
 * A class for artist controller
 */
public class GameController extends ControllerAbstract {

    /***
     * Constructor
     * @throws IOException IOException
     * @throws SQLException SQLException
     */
    public GameController() throws IOException, SQLException{
    }

    /****
     * insert a new game to the data base with the compatible patient
     * @param gameContainer the game info
     */
    public void insertNewGame(GameContainer gameContainer) {
        model.insertNewGame(PatientContainer.getInstance(), gameContainer);
    }

    public ArrayList<GameContainer> getGames(String patientId) {
        return model.getData(patientId, null);
    }
}
