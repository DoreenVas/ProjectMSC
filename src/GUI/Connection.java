package GUI;

import Controller.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * In charge of holding the connections to the controller.
 */
public class Connection {
    private static Connection connection;
    private ControllerInterface patientController;
    private ControllerInterface gameController;

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

    /**
     *
     * Send given information to the controllers.
     * @param map the information for executing a query
     * @param wanted the wanted table string
     * @return the result of the query on given info
     */
//    public TableInfo query(Map<String, ArrayList<String>> map, String wanted) throws SQLException {
//        TableInfo info = null;
//        // check for wanted table
//        switch(wanted) {
//            case "patient":
//                info = this.patientController.getInfoFromGUI(map);
//                break;
//            case "game":
//                info = this.gameController.getInfoFromGUI(map);
//                break;
//        }
//        return info;
//    }
}
