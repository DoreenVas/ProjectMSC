package Model;

import Resources.PatientContainer;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A class holding all of the album table queries, also in charge of executing them.
 */
public class PatientQueries {
    // members
    private static PatientQueries ourInstance = new PatientQueries();
    private static Statement myStatement;
    private static String[] patientColumns = {"patient_id", "patient_name", "patient_gender", "dominant_hand", "birth_date", "patient_type"};

    /****
     * Constructor singleton
     * @param statement a statement
     * @return this instance of albumQueries
     */
    public static PatientQueries getInstance(Statement statement) {
        myStatement = statement;
        return ourInstance;
    }

    /******
     * Request patient information from the DB given patient's ID.
     * @param patientID the patient's ID.
     * @return an array of all the patient info.
     */
    public String[] getPatientInfo(String patientID) {
        try {
            String query = "Select * from patient where patient_id=\"" + patientID + "\";";
            // execute the query
            String[] resultSet = Executor.executeQuery(myStatement, query, patientColumns);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient info - " + e.getMessage());
            return null;
        }
    }

    public void getResultsByPatient(String p_id, String[] tables) {
        try {
            String[] resultSet;
            for (String table : tables) {
                String command = String.format("Select * from %s, patient_game" +
                        " where patient_game.patient_id=\"%s\" and patient_game.game_id=%s.game_id;", table, p_id, table);
                resultSet = Executor.executeQuery(myStatement, command, patientColumns);
            }
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient results - " + e.getMessage());
        }
    }

    /*****
     * Inserts a new patient into the DB, given a patient container.
     * @param patientInfo the information of the patient.
     */
    public void insertNewPatient(PatientContainer patientInfo) {
        String p_id = patientInfo.getPatientID(), p_name = patientInfo.getPatientName(), p_age = patientInfo.getPatientAge();
        String p_hand = patientInfo.getPatientDominantHand(), p_gender = patientInfo.getPatientGender(), p_type = patientInfo.getPatientType();
        try {
            String command = String.format("insert into patient (patient_id, patient_name, patient_gender, dominant_hand," +
                    " birth_date, patient_type)values(\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\")", p_id, p_name, p_gender, p_hand, p_age, p_type);
            // execute the query
            Executor.executeInsertQuery(myStatement, command, patientColumns);
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient info - " + e.getMessage());
        }
    }

    /*****
     * Request all patients' IDs from the DB.
     * @return an array with all the IDs of the patients.
     */
    public String[] getAllPatientsIDs() {
        String[] cols = {"patient_id"};
        String query = "Select patient_id from patient;";
        // execute the query
        String[] resultSet = new String[0];
        try {
            resultSet = Executor.executeQuery(myStatement, query, cols);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
