package Resources;

/**
 * Holds all of the alert messages the application can display.
 */
public class AlertMessages {

    /**
     *
     * Holds a failed connection message to display.
     * @return the failed message content
     */
    public static String failedConnection() {
        return "Couldn't open connection to db.\nTry again?";
    }

    /**
     *
     * Holds a failed connection message to display and add the reason to it.
     * @param reason the reason the error occurred
     * @return the failed message content
     */
    public static String failedConnection(String reason) {
        return "Couldn't open connection to db.\nReason: " + reason + "\nTry again?";
    }

    /**
     * Holds a failed connection message to display and add the reason to it.
     * Also asks the user if should try the action again.
     * @param reason the reason the error occurred
     * @param tryAgain if to display the "try again?" text
     * @return the failed message content
     */
    public static String failedConnection(String reason, boolean tryAgain) {
        String message = "Couldn't open connection to db.\nReason: " + reason;
        if(tryAgain) {
            message += "\nTry again?";
        }
        return message;
    }

    /**
     *
     * Holds a query execution failure message to display.
     * @return the query execution failure message content
     */
    public static String queryExecutionFailure() {
        return "Failed to execute a query.";
    }

    /**
     *
     * Holds a disconnecting failure message to display.
     * @return the disconnecting failure message content
     */
    public static String failedDisconnection() {
        return "Couldn't close the connection to db.\nTry again?";
    }

    /**
     *
     * Holds a page loading failure message to display.
     * @return the page loading failure message content
     */
    public static String pageLoadingFailure() {
        return "Couldn't load the requested page.";
    }

    /**
     *
     * Holds a file reading failure message to display.
     * @param fileName the file failed to read from
     * @return the page loading failure message content
     */
    public static String readFromFileError(String fileName) {
        return "couldn't read from " + fileName + " file.";
    }

    /**
     *
     * Holds a sending map to controller failure message to display.
     * @return the page loading failure message content
     */
    public static String errorSendingMapToController() {
        return "Error sending information to the Controller.";
    }
}
