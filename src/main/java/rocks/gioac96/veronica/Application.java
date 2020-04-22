package rocks.gioac96.veronica;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.ExchangeParserImpl;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.http.SetCookieHeader;
import rocks.gioac96.veronica.routing.Router;

/**
 * Veronica application.
 */
@SuppressWarnings("unused")
public final class Application<Q extends Request, S extends Response> {

    @Getter
    private final int port;

    private final HttpServer server;

    @Getter
    @Setter
    @NonNull
    private Router<Q, S> router;

    @Getter
    @Setter
    @NonNull
    private ExchangeParser<Q> exchangeParser;

    @Getter
    @Setter
    @NonNull
    private ExceptionHandler exceptionHandler;

    protected Application(
        int port,
        @NonNull Router<Q, S> router,
        @NonNull ExchangeParser<Q> exchangeParser,
        @NonNull ExceptionHandler exceptionHandler
    ) throws IOException {

        this.port = port;
        this.router = router;
        this.exchangeParser = exchangeParser;
        this.exceptionHandler = exceptionHandler;

        this.server = HttpServer.create(
            new InetSocketAddress(port), 0
        );
        server.createContext("/", this::handleExchange);

        server.setExecutor(null);

    }

    public static <Q extends Request, S extends Response> ApplicationBuilder<Q, S> builder() {

        return new ApplicationBuilder<Q, S>();

    }

    public static ApplicationBuilder<Request, Response> basic() {

        return new ApplicationBuilder<Request, Response>()
            .exchangeParser(new ExchangeParserImpl())
            .exceptionHandler(new ExceptionHandler() {});

    }

    private void handleExchange(HttpExchange exchange) {

        Response response;

        try {

            // Parse request
            Q request = exchangeParser.parseExchange(exchange);

            // Generate response
            response = router.route(request);


        } catch (Exception e) {

            response = exceptionHandler.handle(e);

        }

        try {
            // Writing response headers
            exchange.getResponseHeaders().putAll(response.getHeaders());

            // Cookies
            List<String> cookieHeaders = new ArrayList<>();

            for (SetCookieHeader httpCookie : response.getCookies()) {

                cookieHeaders.add(httpCookie.toHeaderString());

            }

            exchange.getResponseHeaders().put("Set-Cookie", cookieHeaders);

            // Send response headers
            exchange.sendResponseHeaders(response.getHttpStatus().getCode(), response.getBody().length());

            // Send response body
            exchange.getResponseBody().write(response.getBody().getBytes());

            // Close response
            exchange.close();

        } catch (IOException e) {

            exceptionHandler.handleExchangeException(e);

        }

    }

    /**
     * Starts the application.
     */
    public void start() {

        if (router == null) {

            throw new NullPointerException("Application router must be set before starting the application");

        }

        server.start();

    }

    /**
     * Stops application.
     */
    public void stop() {

        server.stop(1);

    }

    public static class ApplicationBuilder<Q extends Request, S extends Response> {

        private int port;
        private @NonNull Router<Q, S> router;
        private @NonNull ExchangeParser<Q> exchangeParser;
        private @NonNull ExceptionHandler exceptionHandler;

        ApplicationBuilder() {
        }

        public ApplicationBuilder<Q, S> port(int port) {

            this.port = port;
            return this;

        }

        public ApplicationBuilder<Q, S> router(@NonNull Router<Q, S> router) {

            this.router = router;
            return this;

        }

        public ApplicationBuilder<Q, S> exchangeParser(ExchangeParser<Q> exchangeParser) {

            this.exchangeParser = exchangeParser;
            return this;

        }

        public ApplicationBuilder<Q, S> exceptionHandler(ExceptionHandler exceptionHandler) {

            this.exceptionHandler = exceptionHandler;
            return this;

        }

        public Application<Q, S> build() throws IOException {

            return new Application<Q, S>(port, router, exchangeParser, exceptionHandler);

        }

    }

}
