package GUI;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** a node which displays a value on hover*/
class HoveredThresholdNode extends StackPane {

    /*****
     * Constructor
     * @param value the value we wish to display in the label.
     */
    HoveredThresholdNode(double value) {
        setPrefSize(7, 7);

        final Label label = createDataThresholdLabel(value);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    /****
     * the function creates the label
     * @param value a value to display
     * @return a label
     */
    private Label createDataThresholdLabel(double value) {
        final Label label = new Label(value + "");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        label.setTextFill(Color.BLACK);

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}
