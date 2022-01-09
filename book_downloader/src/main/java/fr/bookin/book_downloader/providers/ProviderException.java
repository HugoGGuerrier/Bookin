package fr.bookin.book_downloader.providers;

/**
 * This class represent an exception from the provider
 *
 * @author Emilie SIAU
 * @author Hugo GUERRIER
 */
public class ProviderException extends Exception {
    public ProviderException() {
    }

    public ProviderException(String message) {
        super(message);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderException(Throwable cause) {
        super(cause);
    }

    public ProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
