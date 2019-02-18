package Resources;

public class PatientContainer {
    // members
    private String patientName;
    private String patientID;
    private String patientAge;
    private String patientGender;
    private String patientDominantHand;

    public PatientContainer(String name, String id, String age, String gender, String hand) {
        this.patientName = name;
        this.patientID = id;
        this.patientAge = age;
        this.patientGender = gender;
        this.patientDominantHand = hand;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientDominantHand() {
        return patientDominantHand;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }
}
