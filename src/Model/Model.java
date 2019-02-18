package Model;

import java.sql.SQLException;

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
}
