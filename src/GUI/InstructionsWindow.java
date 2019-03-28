package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InstructionsWindow extends BasicWindow{
    // members
    @FXML
    private Button back = new Button();
    @FXML
    private TextArea instructionsText = new TextArea();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String fileContent = parseInstructions();
        this.instructionsText.setText(fileContent);
        super.initialize(location, resources);
    }

    /*****
     * Parse the information of the instruction file
     */
    private String parseInstructions() {
        String row;
        StringBuilder stringBuilder = new StringBuilder();
        // Current working directory is ProjectMSC
        // the path to the instruction file
        String configFilePath = "src/Resources/instructionsFile";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
            // read the info from the config file
            row = reader.readLine();
            while(row != null) {
                stringBuilder.append(row);
                row = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open instruction file reader\n");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Could not read from instruction file\n");
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @FXML
    protected void mainWindow() {
        super.menuWindow(this.back);
    }

    @FXML
    public void logout() {
        super.logout();
    }
}
