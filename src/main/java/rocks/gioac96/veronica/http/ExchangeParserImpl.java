package rocks.gioac96.veronica.http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * {@link HttpExchange} parser that generates a {@link Request} output object.
 */
public final class ExchangeParserImpl implements ExchangeParser<Request> {

    /**
     * Parses an {@link HttpExchange} to generate a {@link Request} object.
     *
     * @param httpExchange httpExchange to parse
     * @return the generated {@link Request}
     * @throws IOException on parsing failure
     */
    public Request parseExchange(HttpExchange httpExchange) throws IOException {

        HttpMethod httpMethod = HttpMethod.fromName(httpExchange.getRequestMethod());
        String body = new String(httpExchange.getRequestBody().readAllBytes());

        return Request.builder()
            .httpMethod(httpMethod)
            .uri(httpExchange.getRequestURI())
            .headers(httpExchange.getRequestHeaders())
            .body(body)
            .build();

    }

}