package Controller;

import Model.DBModel;
import Model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/***
 * An abstract class for the controller
 */
public abstract class ControllerAbstract implements ControllerInterface {
    // members
    public static Model model;

    /***
     * Constructor for singleton
     * @throws IOException
     */
    public ControllerAbstract() throws IOException {
        this.model = new DBModel();
    }

    /***
     * closes the connection to the database
     * @throws SQLException if closing the connection failed
     */
    public void closeModelConnection() throws SQLException {
        this.model.closeConnection();
    }

    /***
     * opens the connection to the database
     * @throws SQLException if opening the connection to the database failed
     */
    public void openModelConnection() throws SQLException{
        this.model.openConnection();
    }
}
