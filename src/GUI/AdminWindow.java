package GUI;

import Resources.*;
import Model.Connection;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/******
 * Admin window class.
 * Responsible over the admin log in and operations,
 * such as viewing patients results and charts.
 */
public class AdminWindow implements Initializable{
    @FXML
    private TableView<AdminTableInfoContainer> usersTable;
    @FXML
    private JFXButton patientShapePieChart;
    @FXML
    private JFXButton patientTexturesPieChart;
    @FXML
    private JFXButton patientBothPieChart;
    @FXML
    private JFXButton testerShapePieChart;
    @FXML
    private JFXButton testerTexturesPieChart;
    @FXML
    private JFXButton testerBothPieChart;
    @FXML
    private JFXButton exit;
    @FXML
    private ImageView back;

    private double window_height;
    private double window_width;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // add double click option to the table
        // double goes to the patient's personal zone.
        this.usersTable.setRowFactory(tv -> {
            TableRow<AdminTableInfoContainer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    AdminTableInfoContainer clickedRow = row.getItem();
                    submit(clickedRow.getPatientID());
                }
            });
            return row ;
        });

        createUsersTable();
    }

    /****
     * Add rows and columns to the table
     */
    private void createUsersTable() {
        addCols();
        addRows();
    }

    /******
     * For each patient id, we get all the patient info
     * and display it in the table.
     */
    private void addRows() {
        ObservableList<AdminTableInfoContainer> users = FXCollections.observableArrayList();
        Connection conn = null;
        try {
            conn = Connection.getInstance();
            // get all patients ids
            String[] ids = conn.getAllPatientsIDs();
            PatientContainer patient = null;
            // go over the ids
            for(String id : ids) {
                patient = conn.idQuery(id);
                if(patient == null) {
                    continue;
                }
                String[] info = new String[5];
                info[0] = patient.getPatientID();
                info[1] = patient.getPatientGender();
                info[2] = patient.getPatientAge();
                info[3] = patient.getPatientDominantHand();
                info[4] = patient.getPatientType();
                AdminTableInfoContainer container = new AdminTableInfoContainer(info[0], info[1], info[2], info[3], info[4]);
                users.add(container);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // add the patients info to the table
        this.usersTable.getItems().addAll(users);
    }

    /*****
     * Add the columns of the table
     */
    private void addCols() {
        String patient_id_txt = "";
        String patient_gender_txt = "";
        String birth_date_txt = "";
        String dominant_hand_txt = "";
        String patient_type_txt = "";

        switch (MainWindow.language) {
            case "Hebrew":
                patient_id_txt = "תעודת זהות";
                patient_gender_txt = "מין";
                patient_type_txt = "סוג המשתמש";
                birth_date_txt = "תאריך לידה";
                dominant_hand_txt = "יד דומיננטית";
                break;
            case "English":
                patient_id_txt = "User ID";
                patient_gender_txt = "User Gender";
                patient_type_txt = "User type";
                birth_date_txt = "Birth date";
                dominant_hand_txt = "Dominant hand";
                break;
        }

        // create patient id column
        TableColumn<AdminTableInfoContainer, String> patientIDCol = new TableColumn<>("patientID");
        patientIDCol.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patientIDCol.setText(patient_id_txt);
        patientIDCol.setMinWidth(patient_id_txt.length());
        this.usersTable.getSortOrder().add(patientIDCol);
        this.usersTable.getColumns().add(patientIDCol);

        // create patient gender column
        TableColumn<AdminTableInfoContainer, String> patientGenderCol = new TableColumn<>("patientGender");
        patientGenderCol.setCellValueFactory(new PropertyValueFactory<>("patientGender"));
        patientGenderCol.setText(patient_gender_txt);
        patientGenderCol.setMinWidth(patient_gender_txt.length());
        this.usersTable.getSortOrder().add(patientGenderCol);
        this.usersTable.getColumns().add(patientGenderCol);

        // create patient age column
        TableColumn<AdminTableInfoContainer, String> patientBirthDateCol = new TableColumn<>("patientAge");
        patientBirthDateCol.setCellValueFactory(new PropertyValueFactory<>("patientAge"));
        patientBirthDateCol.setText(birth_date_txt);
        patientBirthDateCol.setMinWidth(birth_date_txt.length());
        this.usersTable.getSortOrder().add(patientBirthDateCol);
        this.usersTable.getColumns().add(patientBirthDateCol);

        // create patient dominant hand column
        TableColumn<AdminTableInfoContainer, String> patientDominantHandCol = new TableColumn<>("patientDominantHand");
        patientDominantHandCol.setCellValueFactory(new PropertyValueFactory<>("patientDominantHand"));
        patientDominantHandCol.setText(dominant_hand_txt);
        patientDominantHandCol.setMinWidth(dominant_hand_txt.length());
        this.usersTable.getSortOrder().add(patientDominantHandCol);
        this.usersTable.getColumns().add(patientDominantHandCol);

        // create patient type column
        TableColumn<AdminTableInfoContainer, String> patientTypeCol = new TableColumn<>("patientType");
        patientTypeCol.setCellValueFactory(new PropertyValueFactory<>("patientType"));
        patientTypeCol.setText(patient_type_txt);
        patientTypeCol.setMinWidth(patient_type_txt.length());
        this.usersTable.getSortOrder().add(patientTypeCol);
        this.usersTable.getColumns().add(patientTypeCol);
    }

    /******
     * In order to search for a certain patient the admin must ype a correct ID of a patient
     * (i.e 9 digits). After verifying the ID, the personal zone of the patient will be shown.
     * @param p_id the id of the patient
     */
    private void submit(String p_id) {
        try {
            // check validation of id number
            int id = Integer.parseInt(p_id);
            if (id < 0 || id > 999999999) {
                throw new NumberFormatException();
            }
            // get the information of the patient
            Connection conn = Connection.getInstance();
            PatientContainer p_info = conn.idQuery(p_id);
            // patient doesn't exist in the database
            if (p_info == null) {
                switch (MainWindow.language) {
                    case "Hebrew":
//                        Alerter.showAlert("תעודת זהות לא במערכת. נסה שנית.", Alert.AlertType.WARNING);
//                        break;
                    case "English":
                        Alerter.showAlert("Id is not in the system. Try again.", Alert.AlertType.WARNING);
                        break;
                }
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(MainWindow.language+"/GraphsWindow.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage stage = (Stage) this.usersTable.getScene().getWindow();
                // get the size of the screen
                Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                this.window_height = window.height;
                this.window_width = window.width;
                // set the window size
                Scene scene = new Scene(root,  this.window_width,  this.window_height);
                scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
                GraphsWindow graphsWindow = loader.getController();
                graphsWindow.setPreviousScene(MainWindow.language+"/AdminWindow.fxml");
                stage.setTitle("Patient Data");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setMaximized(true);
                stage.show();
            }
        } catch (NumberFormatException e) {
            switch (MainWindow.language) {
                case "Hebrew":
//                    Alerter.showAlert("תעודת זהות לא תקינה! נסה שוב.", Alert.AlertType.WARNING);
//                    break;
                case "English":
                    Alerter.showAlert("Invalid Id. Please try again.", Alert.AlertType.WARNING);
                    break;
            }
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }

    /*****
     * the function shows a pie chart of errors, according to the game type.
     * the game type is chosen by clicking different buttons
     * @param gameType the game type (Shapes/ Textures/ Both).
     */
    private void showPieChart(Boolean isTester, String gameType) {
        Stage secondStage = new Stage();
        VBox vBox = new VBox();
        AnchorPane anchorPane = new AnchorPane();
        // create the bar chart
        PieChart pieChart = PieChartBuilder.createChart(isTester, gameType);
        // add a label for clicking - showing percentage
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double total = 0;
                        for (PieChart.Data d : pieChart.getData()) {
                            total += d.getPieValue();
                        }
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100*data.getPieValue()/total) ;
                        caption.setText(text);
                    }
            );
        }
        // add the chart and the label
        anchorPane.getChildren().addAll(pieChart, caption);
        Scene scene = new Scene(vBox, 600, 600);
        scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
        // bind the bar chart to the size of the window
        pieChart.minWidthProperty().bind(anchorPane.widthProperty());
        pieChart.minHeightProperty().bind(anchorPane.heightProperty().subtract(20));
        anchorPane.minWidthProperty().bind(scene.widthProperty());
        anchorPane.minHeightProperty().bind(scene.heightProperty().subtract(20));
        // add a description label to the window
        Label description = new Label();
        switch (MainWindow.language) {
            case "Hebrew":
                description.setText("לחץ על הגרף לקבלת אחוזים");
                break;
            case "English":
                description.setText("Press on the chart to review percentages");
                break;
        }
        description.setFont(Font.font(20));
        // add the chart and the label to the scene
        vBox.getChildren().addAll(anchorPane, description);
        vBox.setAlignment(Pos.CENTER);
        VBox.setMargin(description, new Insets(0, 10, 10, 10));
        // show the window
        secondStage.setScene(scene);
        secondStage.show();
    }

    /*******
     * By clicking each of the buttons in the window, we show a matching pie chart (according to the
     * game type), that represent the success/failure rate of all the patients.
     */
    @FXML
    private void patientShapesPieChart() {
        showPieChart(false, "Shapes");
    }

    @FXML
    private void patientTexturesPieChart() {
        showPieChart(false, "Textures");
    }

    @FXML
    private void patientBothPieChart() {
        showPieChart(false, "Both");
    }

    @FXML
    private void testerShapesPieChart() {
        showPieChart(true, "Shapes");
    }

    @FXML
    private void testerTexturesPieChart() {
        showPieChart(true, "Textures");
    }

    @FXML
    private void testerBothPieChart() {
        showPieChart(true, "Both");
    }

    /***
     * Exiting the application.
     */
    @FXML
    protected void exit() {
        Stage stage = (Stage) this.exit.getScene().getWindow();
        stage.close();
    }

    /********
     * Clicking this button will log out from the admin window and return to the main window.
     */
    @FXML
    protected void back() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(MainWindow.language + "/MainWindow.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Stage stage = (Stage) this.usersTable.getScene().getWindow();
            // get the size of the screen
            Rectangle window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            this.window_height = window.height;
            this.window_width = window.width;
            // set the window size
            Scene scene = new Scene(root, this.window_width, this.window_height);
            scene.getStylesheets().add(getClass().getResource("BasicCSS.css").toExternalForm());
            stage.setTitle("Patient Data");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            Alerter.showAlert(AlertMessages.pageLoadingFailure(), Alert.AlertType.ERROR);
        }
    }
}
