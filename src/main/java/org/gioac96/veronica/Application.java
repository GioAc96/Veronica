package org.gioac96.veronica;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.RequestParser;
import org.gioac96.veronica.routing.Router;

public class Application {

    @Getter
    protected final int port;

    protected HttpServer server;

    protected RequestParser requestParser;

    @Getter
    @Setter
    @NonNull
    protected Router router;

    public Application(int port) throws IOException {

        this.port = port;
        this.requestParser = new RequestParser();

        initServer();

    }

    public Application(int port, @NonNull Router router) throws IOException {

        this.port = port;
        this.requestParser = new RequestParser();
        this.router = router;

        initServer();

    }

    private void initServer() throws IOException {

        HttpHandler httpHandler = exchange -> {

            // Parse request
            Request request = requestParser.parseExchange(exchange);

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

    public void start() {

        if (router == null) {

            throw new NullPointerException("Application router must be set before starting the application");

        }

        server.start();

    }

    public void stop() {

        server.stop(1);


    }

}
