package GUI;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class InstructionsWindow extends BasicWindow{
    // members
    @FXML
    Button back = new Button();

    @FXML
    protected void mainWindow() {
        super.menuWindow(this.back);
    }
}
