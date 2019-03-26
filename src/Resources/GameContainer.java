package Resources;

import java.util.HashMap;

public class GameContainer {
    // members
    private HashMap<String, Double> shapesReactionTime;
    private HashMap<String, Double> texturesReactionTime;
    private int numOfRecognizedButtons, timeLimit;
    private String gameType, gameDate = null;

    public GameContainer(HashMap<String, Double> shapesReactionTime, HashMap<String, Double> texturesReactionTime,
                         int numOfRecognizedButtons, int timeLimit, String gameType) {
        this.gameType = gameType;
        this.numOfRecognizedButtons = numOfRecognizedButtons;
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

    public int getNumOfRecognizedButtons() {
        return this.numOfRecognizedButtons;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

    public String getGameType() {
        return this.gameType;
    }

    public String getGameDate() {
        return this.gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
}
