package org.gioac96.veronica;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.HttpExchangeParser;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.Router;

/**
 * Veronica application.
 */
public class Application {

    @Getter
    protected final int port;

    protected HttpServer server;

    protected HttpExchangeParser httpExchangeParser;

    @Getter
    @Setter
    @NonNull
    protected Router router;

    public Application(int port) throws IOException {

        this.port = port;
        this.httpExchangeParser = new HttpExchangeParser();

        initServer();

    }

    public Application(int port, @NonNull Router router) throws IOException {

        this.port = port;
        this.httpExchangeParser = new HttpExchangeParser();
        this.router = router;

        initServer();

    }

    private void initServer() throws IOException {

        HttpHandler httpHandler = exchange -> {

            // Parse request
            Request request = httpExchangeParser.parseExchange(exchange);

            // Generate response
            Response response = router.route(request);

            // Send response headers
            exchange.sendResponseHeaders(response.getHttpStatus().getCode(), response.getBody().length());

            // Send response body
            exchange.getResponseBody().write(response.getBody().getBytes());

            // Close response
            exchange.close();

        };

        server = HttpServer.create(
            new InetSocketAddress(port), 0
        );
        server.createContext("/", httpHandler);

        server.setExecutor(null);

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
