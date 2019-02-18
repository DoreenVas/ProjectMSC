package Resources;
import java.sql.SQLException;

public class DBConnectionException extends SQLException {

    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBConnectionException(String message) {
        super(message);
    }

    public DBConnectionException(Throwable cause) {
        super(cause);
    }

    public DBConnectionException() {
        super();
    }
}
