package rocks.gioac96.veronica.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import lombok.Getter;

/**
 * {@link HttpExchange} parser that generates a {@link Request} output object.
 */
@SuppressWarnings("checkstyle:RightCurly")
public final class BasicExchangeParser {

    @Getter()
    private static final ExchangeParser<Request> instance = httpExchange -> {
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
    };

    @SuppressWarnings("CheckStyle:RightCurly")
    private BasicExchangeParser() {}

}
