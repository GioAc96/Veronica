package org.gioac96.veronica;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.RequestParser;

public class Application {

    private final transient int port;
    private transient HttpServer server;

    private RequestParser requestParser;

    public Application(int port) throws IOException {

        this.port = port;
        this.requestParser = new RequestParser();

        initServer();

    }

    private void initServer() throws IOException {

        HttpHandler httpHandler = exchange -> {

            // Parse request
            Request request = requestParser.parseExchange(exchange);

            // Generate response
            Response response = Response.builder()
                .httpStatus(HttpStatus.OK)
                .body(request.getBody())
                .build();

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

        server.start();

    }

    public void stop() {

        server.stop(1);

    }

}
