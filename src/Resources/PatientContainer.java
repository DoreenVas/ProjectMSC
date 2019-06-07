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
    private String patientType;

    /*****
     * Constructor
     */
    public PatientContainer() {
        this.patientName = null;
        this.patientID = null;
        this.patientAge = null;
        this.patientGender = null;
        this.patientDominantHand = null;
        this.patientType = null;
    }

    /****
     * Singleton implement.
     * @return the current instance of the patient container.
     */
    public static PatientContainer getInstance()
    {
        if (patientContainerInstance == null)
            patientContainerInstance = new PatientContainer();

        return patientContainerInstance;
    }

    /*****
     * Returns the age of the patient.
     * @return the age of the patient.
     */
    public String getPatientAge() {
        return patientAge;
    }

    /*****
     * Returns the id of the patient.
     * @return the id of the patient.
     */
    public String getPatientID() {
        return patientID;
    }

    /*****
     * Returns the dominant hand of the patient.
     * @return the dominant hand of the patient.
     */
    public String getPatientDominantHand() {
        return patientDominantHand;
    }

    /*****
     * Returns the name of the patient.
     * @return the name of the patient.
     */
    public String getPatientName() {
        return patientName;
    }

    /*****
     * Returns the gender of the patient.
     * @return the gender of the patient.
     */
    public String getPatientGender() {
        return patientGender;
    }

    /*****
     * Returns the type of the patient(tester ot patient).
     * @return the type of the patient.
     */
    public String getPatientType() { return patientType; }

    /******
     * Sets the type of the patient.
     * @param patientType the new type of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientType(String patientType) {
        this.patientType = patientType;
        return this;
    }

    /******
     * Sets the name of the patient.
     * @param name the new name of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientName(String name) {
        this.patientName = name;
        return this;
    }

    /******
     * Sets the id of the patient.
     * @param id the new id of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientID(String id) {
        this.patientID = id;
        return this;
    }

    /******
     * Sets the age of the patient.
     * @param age the new name of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientAge(String age) {
        this.patientAge = age;
        return this;
    }

    /******
     * Sets the gender of the patient.
     * @param gender the new name of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientGender(String gender) {
        this.patientGender = gender;
        return this;
    }

    /******
     * Sets the dominant Hand of the patient.
     * @param dominantHand the new name of the patient.
     * @return the current patient container.
     */
    public PatientContainer setPatientDominantHand(String dominantHand) {
        this.patientDominantHand = dominantHand;
        return this;
    }

    /****
     * In event of log out, we reset the patient container.
     */
    public void logOut() {
        this.patientContainerInstance = null;
    }

    /****
     * Return the patient name as a property.
     * @return the patient name as a property.
     */
    public StringProperty valueProperty(){
        return new SimpleStringProperty(this.patientName);
    }
}
