package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/******
 * Instructions window class.
 * In this window we load the instructions of the game from
 * a file, and display them to the screen.
 */
public class InstructionsWindow extends BasicWindow{
    // members
    @FXML
    private ImageView back;
    @FXML
    private TextArea instructionsText = new TextArea();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(MainWindow.language.equals("English")){
            String fileContent = parseInstructions();
            this.instructionsText.setText(fileContent);
        }
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
        String filePath = "";
        switch (MainWindow.language) {
            case "Hebrew":
                filePath = "instructionsFileHeb";
                break;
            case "English":
                filePath = "instructionsFileEng";
                break;
        }
        // read the instructions from the file
        InputStream instructionsFilePath = this.getClass().getClassLoader().getResourceAsStream(filePath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(instructionsFilePath));
            // read the info from the config file
            row = reader.readLine();
            while(row != null) {
                stringBuilder.append(row);
                stringBuilder.append("\n");
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
