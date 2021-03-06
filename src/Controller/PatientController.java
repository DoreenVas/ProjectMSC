package Controller;

import Resources.PatientContainer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

    /*****
     * returns patient container with all the info of the current patient
     * @param id the id of the patient
     * @return a patient container
     */
    public PatientContainer getInfoFromGUI(String id) {
        PatientContainer patientContainer = model.getData(id);
        return patientContainer;
    }

    /****
     * insert a new patient to the data base
     * @param patientContainer the patient info
     */
    public void insertNewPatient(PatientContainer patientContainer) {
        model.insertNewPatient(patientContainer);
    }

    /*****
     * Request all patients' IDs from the model.
     * @return an array with all the IDs of the patients.
     */
    public String[] getAllPatientsIDs() {
        return model.getAllPatientsIDs();
    }
}
