package rocks.gioac96.veronica.http;

import java.io.IOException;

public class ExchangeParseException extends IOException {

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
