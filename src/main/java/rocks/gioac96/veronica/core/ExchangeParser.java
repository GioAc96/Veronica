package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;

/**
 * {@link HttpExchange} parser. Parses the exchange to generate a {@link Request} object.
 */
public interface ExchangeParser {

    /**
     * Parses an Http exchange and returns a request.
     *
     * @param httpExchange the exchange to parse
     * @return the parsed exchange
     */
    default Request parseExchange(HttpExchange httpExchange) {

        try {

            HttpMethod httpMethod = HttpMethod.fromName(httpExchange.getRequestMethod());

            String body = new String(httpExchange.getRequestBody().readAllBytes());

            return Request.builder()
                .secure(httpExchange instanceof HttpsExchange)
                .httpMethod(httpMethod)
                .uri(httpExchange.getRequestURI())
                .headers(httpExchange.getRequestHeaders())
                .body(body)
                .build();

        } catch (Exception e) {

            throw new ExchangeParseException("Failed to parse request", e);

        }

    }

}
