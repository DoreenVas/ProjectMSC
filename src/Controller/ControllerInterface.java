package Controller;

//import Resources.TableInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/***
 * An interface for the controller
 */
public interface ControllerInterface {

    /***
     * the function parse he info from the gui and inserts it into tableInfo object
     * @param infoFromGUI a map of key -> values from the gui
     * @return a tableInfo object with all the results according to the data from the gui
     * @throws SQLException throws an error if query failed
     */
//    TableInfo getInfoFromGUI(Map<String, ArrayList<String>> infoFromGUI) throws SQLException;

    /***
     * closes the connection to the database
     * @throws SQLException throws an sql error if closing connection failed
     */
    void closeModelConnection() throws SQLException;

    /***
     * opens the connection to the database
     * @throws SQLException throws an sql error if connection failed
     */
    void openModelConnection() throws SQLException;
}
