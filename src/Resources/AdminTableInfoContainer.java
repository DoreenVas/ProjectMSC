package Resources;

/*****
 * A class for patients table in te admin page
 */
public class AdminTableInfoContainer {
    // members
    private String patientID;
    private String patientGender;
    private String patientAge;
    private String patientDominantHand;
    private String patientType;

    // Constructor
    public AdminTableInfoContainer(String id, String gender, String age, String dominant_hand, String type) {
        this.patientID = id;
        this.patientGender = gender;
        this.patientAge = age;
        this.patientDominantHand = dominant_hand;
        this.patientType = type;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String p_id) {
        this.patientID = p_id;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String p_gender) {
        this.patientGender = p_gender;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String p_age) {
        this.patientAge = p_age;
    }

    public String getPatientDominantHand() {
        return patientDominantHand;
    }

    public void setPatientDominantHand(String p_dominant_hand) {
        this.patientDominantHand = p_dominant_hand;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String p_type) {
        this.patientType = p_type;
    }

}
