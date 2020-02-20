package rocks.gioac96.veronica;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.ExchangeParserImpl;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Router;

/**
 * Veronica application.
 */
public final class Application<Q extends Request, S extends Response> {

    @Getter
    private final int port;

    private final HttpServer server;

    @NonNull
    @Getter
    @Setter
    private ExchangeParser<Q> exchangeParser;

    @Getter
    @Setter
    private Router<Q, S> router;

    @Builder
    protected Application(
        int port,
        @NonNull Router<Q, S> router,
        ExchangeParser<Q> exchangeParser
    ) throws IOException {

        this.port = port;
        this.exchangeParser = exchangeParser;
        this.router = router;
        this.server = HttpServer.create(
            new InetSocketAddress(port), 0
        );
        server.createContext("/", getDefaultHttpHandler());

        server.setExecutor(null);

    }

    /**
     * Instantiates a basic Application, with support for basic Requests and Responses.
     *
     * @param port port to bind to the Http server
     * @param router router of the application
     * @return the instantiated Application
     * @throws IOException on port binding failure
     */
    public static Application<Request, Response> basic(
        int port,
        Router<Request, Response> router
    ) throws IOException {

        return new Application<>(
            port,
            router,
            new ExchangeParserImpl()
        );

    }

    private HttpHandler getDefaultHttpHandler() {

        return exchange -> {

            // Parse request
            Q request = exchangeParser.parseExchange(exchange);

            // Generate response
            S response = router.route(request);

            // Setting response headers
            exchange.getResponseHeaders().putAll(response.getHeaders());

            // Send response headers
            exchange.sendResponseHeaders(response.getHttpStatus().getCode(), response.getBody().length());

            // Send response body
            exchange.getResponseBody().write(response.getBody().getBytes());

            // Close response
            exchange.close();

        };

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

}
