package rocks.gioac96.veronica.core;

/**
 * Exception for exchange parsing. Thrown by {@link ExchangeParser} on parsing failure.
 */
public class ExchangeParseException extends RuntimeException {

    public ExchangeParseException() {

    }

    public ExchangeParseException(String message) {

        super(message);

    }

    public ExchangeParseException(String message, Throwable cause) {

        super(message, cause);

    }

    public ExchangeParseException(Throwable cause) {

        super(cause);

    }

}
