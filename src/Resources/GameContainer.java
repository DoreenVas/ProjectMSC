package Resources;

import java.util.HashMap;

public class GameContainer {
    // members
    private HashMap<String, Double> shapesReactionTime;
    private HashMap<String, Double> texturesReactionTime;
    private int numOfRecognizedButtons, timeLimit;
    private String gameType, gameDate = null, dominantHand;

    /******
     * Constructor
     * @param shapesReactionTime a map of shape -> reaction time
     * @param texturesReactionTime a map of texture -> reaction time
     * @param numOfRecognizedButtons the number of recognized images in the game
     * @param timeLimit the time limit of the game
     * @param gameType the game type
     * @param dominantHand an indication if the player used his dominant hand or not
     */
    public GameContainer(HashMap<String, Double> shapesReactionTime, HashMap<String, Double> texturesReactionTime,
                         int numOfRecognizedButtons, int timeLimit, String gameType, String dominantHand) {
        this.gameType = gameType;
        this.numOfRecognizedButtons = numOfRecognizedButtons;
        this.shapesReactionTime = shapesReactionTime;
        this.texturesReactionTime = texturesReactionTime;
        this.timeLimit = timeLimit;
        this.dominantHand = dominantHand;
    }

    /*****
     * returns a map of shape -> reaction time
     * @return a map
     */
    public HashMap<String, Double> getShapesReactionTime() {
        return this.shapesReactionTime;
    }

    /*****
     * returns a map of texture -> reaction time
     * @return a map
     */
    public HashMap<String, Double> getTexturesReactionTime() {
        return this.texturesReactionTime;
    }

    /*****
     * returns the number of recognized images in the game
     * @return a number
     */
    public int getNumOfRecognizedButtons() {
        return this.numOfRecognizedButtons;
    }

    /*****
     * returns the time limit of the game
     * @return a number
     */
    public int getTimeLimit() {
        return this.timeLimit;
    }

    /*****
     * returns the game type
     * @return a string indicating the game type ("צורות"/"משולב"/"מרקמים")
     */
    public String getGameType() {
        return this.gameType;
    }

    /*****
     * returns the date of when the game was played
     * @return a date
     */
    public String getGameDate() {
        return this.gameDate;
    }

    /*****
     * sets the date of the game
     * @param gameDate the game date
     */
    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    /*****
     * returns an indication if the player used his dominant hand in the game or not ("כן"/"לא")
     * @return an string indication
     */
    public String getDominantHand() {
        return this.dominantHand;
    }
}
