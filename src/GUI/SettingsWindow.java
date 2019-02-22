package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsWindow extends BasicWindow {
    // members
    @FXML
    private Button home = new Button();


    @FXML
    protected void mainWindow() {
        super.menuWindow(this.home);
    }

    public void logout() {
        super.logout();
    }
}
