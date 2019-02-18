package Controller;

import Model.DBModel;
import Model.PatientQueries;
import Resources.PatientContainer;

import java.io.IOException;
import java.sql.SQLException;

/***
 * A patient controller class
 */
public class PatientController extends ControllerAbstract {

    /***
     * Constructor
     * @throws IOException IOException
     * @throws SQLException SQLException
     */
    public PatientController() throws IOException,SQLException {
    }

    public PatientContainer getInfoFromGUI(String id) {
        PatientContainer patientContainer = model.getData(id);
        return patientContainer;
    }

    public void insertNewPatient(PatientContainer patientContainer) {
        model.insertNewPatient(patientContainer);
    }

}
