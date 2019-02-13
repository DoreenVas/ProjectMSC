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
        return shapesReactionTime;
    }

    public HashMap<String, Double> getTexturesReactionTime() {
        return texturesReactionTime;
    }

    public int getNumOfRecginizedButtons() {
        return numOfRecginizedButtons;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public String getGameType() {
        return gameType;
    }
}
