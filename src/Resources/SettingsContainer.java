package Resources;

public class SettingsContainer {
    // members
    private static SettingsContainer settingsContainerInstance = null;

    private String gameType;
    private String timeLimit;
    private String keyboard;

    public SettingsContainer() {
        this.gameType = null;
        this.timeLimit = null;
        this.keyboard = null;
    }

    public static SettingsContainer getInstance()
    {
        if (settingsContainerInstance == null)
            settingsContainerInstance = new SettingsContainer();

        return settingsContainerInstance;
    }

    public String getGameType() {
        return gameType;
    }

    public SettingsContainer setGameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public SettingsContainer setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public String getKeyboard() {
        return keyboard;
    }

    public SettingsContainer setKeyboard(String keyboard) {
        this.keyboard = keyboard;
        return this;
    }
}
