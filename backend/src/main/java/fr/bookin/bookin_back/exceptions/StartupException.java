package fr.bookin.bookin_back.exceptions;

/**
 * This class is an exception that represent the fail of the web application starting
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class StartupException extends RuntimeException {
    public StartupException() {
    }

    public StartupException(String message) {
        super(message);
    }

    public StartupException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartupException(Throwable cause) {
        super(cause);
    }

    public StartupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
