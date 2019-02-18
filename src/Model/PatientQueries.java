package Model;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class holding all of the album table queries, also in charge of executing them.
 */
public class PatientQueries {
    // members
    private static PatientQueries ourInstance = new PatientQueries();
    private static Statement myStatement;
    private static String[] patientColumns = {"patient_id", "patient_name", "patient_gender", "dominant_hand", "patient_age"};

    /****
     * Constructor singleton
     * @param statement a statement
     * @return this instance of albumQueries
     */
    public static PatientQueries getInstance(Statement statement) {
        myStatement = statement;
        return ourInstance;
    }

    public void getPatientInfo(String patientID) {
        try {
            String query = "Select * from patient where patient_id=\"" + patientID + "\";";
            // execute the query
            String[] resultSet = Executor.executeQuery(myStatement, query, patientColumns);

            // TODO
        } catch (SQLException e) {
            System.out.println("ERROR executeQuery in get patient info - " + e.getMessage());
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
}
