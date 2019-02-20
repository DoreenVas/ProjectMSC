package Resources;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/****
 * A class for the patient. Singleton
 */
public class PatientContainer {
    // members
    private static PatientContainer patientContainerInstance = null;

    private String patientName;
    private String patientID;
    private String patientAge;
    private String patientGender;
    private String patientDominantHand;

    public PatientContainer() {
        this.patientName = null;
        this.patientID = null;
        this.patientAge = null;
        this.patientGender = null;
        this.patientDominantHand = null;
    }

    public static PatientContainer getInstance()
    {
        if (patientContainerInstance == null)
            patientContainerInstance = new PatientContainer();

        return patientContainerInstance;
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

    public PatientContainer setPatientName(String name) {
        this.patientName = name;
        return this;
    }

    public PatientContainer setPatientID(String id) {
        this.patientID = id;
        return this;
    }

    public PatientContainer setPatientAge(String age) {
        this.patientAge = age;
        return this;
    }

    public PatientContainer setPatientGender(String gender) {
        this.patientGender = gender;
        return this;
    }

    public PatientContainer setPatientDominantHand(String dominantHand) {
        this.patientDominantHand = dominantHand;
        return this;
    }

    public void logOut() {
        this.patientContainerInstance = null;
    }

    public StringProperty valueProperty(){
        return new SimpleStringProperty(this.patientName);
    }
}
