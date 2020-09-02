package rocks.gioac96.veronica;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.http.SetCookieHeader;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Veronica application.
 */
@SuppressWarnings("unused")
public final class Application {

    @Getter
    @Setter
    @NonNull
    private Router router;

    @Getter
    @Setter
    @NonNull
    private ExchangeParser exchangeParser;

    @Getter
    @Setter
    @NonNull
    private ExceptionHandler exceptionHandler;

    private final ThreadPoolExecutor threadPool;

    private final Set<HttpServer> httpServers;

    protected Application(
        @NonNull Set<Server> servers,
        @NonNull Router router,
        @NonNull ExchangeParser exchangeParser,
        @NonNull ExceptionHandler exceptionHandler,
        @NonNull ThreadPoolExecutor threadPool
    ) throws IOException {

        this.threadPool = threadPool;

        this.router = router;
        this.router.setThreadPool(threadPool);

        this.exceptionHandler = exceptionHandler;
        this.exchangeParser = exchangeParser;

        this.httpServers = new ArraySet<>();

        for (Server server : servers) {

            this.httpServers.add(server.toHttpServer(this::handleExchange));

        }

    }

    /**
     * Instantiates a generic application builder.
     *
     * @return the instantiated generic application builder
     */
    public static  ApplicationBuilder builder() {

        return new ApplicationBuilder();

    }

    /**
     * Instantiates a basic application builder.
     *
     * @return the instantiated basic application builder
     */
    public static ApplicationBuilder basic() {

        return builder()
            .exchangeParser(new ExchangeParser() {})
            .exceptionHandler(new ExceptionHandler() {
            });

    }

    private void handleExchange(HttpExchange exchange) {

        threadPool.submit(() -> {

            Response response;

            try {

                // Parse request
                Request request = exchangeParser.parseExchange(exchange);

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
                exchange.sendResponseHeaders(response.getHttpStatus().getCode(), response.getBody().length);

                // Send response body
                exchange.getResponseBody().write(response.getBody());

                // Close response
                exchange.close();

            } catch (IOException e) {

                exceptionHandler.handleExchangeException(e);

            }

        });

    }

    /**
     * Starts the application.
     */
    public void start() {

        for (HttpServer httpServer : httpServers) {

            httpServer.start();

        }

    }

    /**
     * Stops application.
     */
    public void stop() {

        for (HttpServer httpServer : httpServers) {

            httpServer.stop(1);

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "UnusedReturnValue"})
    public static class ApplicationBuilder {

        private Router router;
        private ExchangeParser exchangeParser;
        private ExceptionHandler exceptionHandler;
        private final Set<Server> servers = new HashSet<>();
        private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        ApplicationBuilder() {
        }

        public ApplicationBuilder router(@NonNull Router router) {

            this.router = router;
            return this;

        }

        public ApplicationBuilder exchangeParser(ExchangeParser exchangeParser) {

            this.exchangeParser = exchangeParser;
            return this;

        }

        public ApplicationBuilder exceptionHandler(ExceptionHandler exceptionHandler) {

            this.exceptionHandler = exceptionHandler;
            return this;

        }

        public ApplicationBuilder port(int port) {

            return server(Server.builder().port(port).build());

        }

        public ApplicationBuilder server(@NonNull Server server) {

            this.servers.add(server);
            return this;

        }

        public ApplicationBuilder threadPool(@NonNull ThreadPoolExecutor threadPool) {

            this.threadPool = threadPool;
            return this;

        }

        public Application build() {

            try {

                return new Application(
                    servers,
                    router,
                    exchangeParser,
                    exceptionHandler,
                    threadPool
                );

            } catch (IOException e) {

                throw new CreationException(e);

            }

        }

    }

}
