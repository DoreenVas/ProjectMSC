package Resources;

public class PatientContainer {
    // memebers
    private String patientName;
    private String patientID;
    private String patientAge;
    private char patientGender;
    private char patientDominantHand;

    public PatientContainer(String name, String id, String age, char gender, char hand) {
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

    public char getPatientDominantHand() {
        return patientDominantHand;
    }

    public String getPatientName() {
        return patientName;
    }

    public char getPatientGender() {
        return patientGender;
    }
}
