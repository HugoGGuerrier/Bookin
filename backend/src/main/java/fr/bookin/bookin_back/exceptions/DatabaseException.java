package fr.bookin.bookin_back.exceptions;

/**
 * This exception represents an error on the database manipulation
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class DatabaseException extends Exception {
    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(Throwable cause) {
        super(cause);
    }

    public DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
