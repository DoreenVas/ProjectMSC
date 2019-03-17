package Resources;

import java.util.HashMap;

public class GameContainer {
    // members
    private HashMap<String, Double> shapesReactionTime;
    private HashMap<String, Double> texturesReactionTime;
    private int numOfRecginizedButtons, timeLimit;
    private String gameType;

    public GameContainer(HashMap<String, Double> shapesReactionTime, HashMap<String, Double> texturesReactionTime,
                         int numOfRecginizedButtons, int timeLimit, String gameType) {
        this.gameType = gameType;
        this.numOfRecginizedButtons = numOfRecginizedButtons;
        this.shapesReactionTime = shapesReactionTime;
        this.texturesReactionTime = texturesReactionTime;
        this.timeLimit = timeLimit;
    }

    public HashMap<String, Double> getShapesReactionTime() {
        return this.shapesReactionTime;
    }

    public HashMap<String, Double> getTexturesReactionTime() {
        return this.texturesReactionTime;
    }

    public int getNumOfRecginizedButtons() {
        return this.numOfRecginizedButtons;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

    public String getGameType() {
        return this.gameType;
    }
}
