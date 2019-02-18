package Controller;

import java.sql.SQLException;

/***
 * An interface for the controller
 */
public interface ControllerInterface {

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
