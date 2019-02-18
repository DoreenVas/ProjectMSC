package GUI;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Holds all of the alerts the application can invoke.
 */
public class Alerter {

    /**
     *
     * Show an alert message of given type with given message.
     * @param message the message to display
     * @param type the wanted alert type
     */
    public static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     *
     * Show an alert message of given type with given message.
     * Also alters the alerts buttons to given buttonTypes.
     * @param message the message to display
     * @param type the wanted alert type
     * @param buttonTypes the wanted buttons to display
     * @return True or False according to the clicked button by the user
     */
    public static boolean showAlert(String message, Alert.AlertType type, String buttonTypes) {
        Alert alert = new Alert(type, message);
        ButtonType yes = new ButtonType("Yes"), no = new ButtonType("No");
        // check wanted buttons
        if(buttonTypes.replace(" ", "").equals("yesno") ||
                buttonTypes.replace(" ", "").equals("noyes")) {
            // clear alert buttons and add wanted buttons
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(yes);
            alert.getButtonTypes().add(no);
        }
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get().equals(yes);
    }
}
