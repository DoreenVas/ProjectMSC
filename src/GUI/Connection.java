package GUI;

import Controller.*;
import Model.PatientQueries;
import Resources.GameContainer;
import Resources.PatientContainer;
import java.io.IOException;
import java.sql.SQLException;

/**
 * In charge of holding the connections to the controller.
 */
public class Connection {
    private static Connection connection;
    private PatientController patientController;
    private GameController gameController;

    /**
     *
     * Returns the classes instance.
     * @return the classes current instance
     * @throws IOException thrown from inner function
     * @throws SQLException thrown from inner function
     */
    public static Connection getInstance() throws IOException, SQLException {
        if(connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    /**
     *
     * Constructor.
     * @throws IOException thrown from inner function
     * @throws SQLException thrown from inner function
     */
    private Connection() throws IOException, SQLException {
        patientController = new PatientController();
        gameController = new GameController();
    }

    /**
     *
     * Opens a connection to the controllers.
     * @throws SQLException thrown from inner function
     */
    public void OpenConnection() throws SQLException {
        this.patientController.openModelConnection();
    }

    /**
     *
     * Disconnects from the controllers.
     * @throws SQLException thrown from inner function
     */
    public void CloseConnection() throws SQLException {
        this.patientController.closeModelConnection();
    }


    public PatientContainer idQuery(String patientId) throws SQLException {
        PatientContainer info;
        info = this.patientController.getInfoFromGUI(patientId);
        return info;
    }

    public void insertPatientQuery(PatientContainer patientContainer){
        this.patientController.insertNewPatient(patientContainer);
    }


//    public GameContainer gameQuery(String patientId) throws SQLException {
//        GameContainer info = null;
//        info = this.gameController.getInfoFromGUI(map);
//        return info;
//    }

}
