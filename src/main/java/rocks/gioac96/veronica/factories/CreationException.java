package rocks.gioac96.veronica.factories;

public class CreationException extends RuntimeException {

    public CreationException() {
    }

    public CreationException(String message) {
        super(message);
    }

    public CreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreationException(Throwable cause) {
        super(cause);
    }

    public CreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
