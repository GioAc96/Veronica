package rocks.gioac96.veronica.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * {@link HttpExchange} parser. Parses the exchange to generate a {@link Request} object.
 * @param <Q> Type of the Request
 */
public interface ExchangeParser<Q extends Request> {

    Q parseExchange(HttpExchange httpExchange) throws IOException;

}
