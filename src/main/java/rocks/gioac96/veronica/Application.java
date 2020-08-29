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
import rocks.gioac96.veronica.http.BasicExchangeParser;
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
public final class Application<Q extends Request, S extends Response> {

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

    private final ThreadPoolExecutor threadPool;

    private final Set<HttpServer> httpServers;

    protected Application(
        @NonNull Set<Server> servers,
        @NonNull Router<Q, S> router,
        @NonNull ExchangeParser<Q> exchangeParser,
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
     * @param <Q> Request type
     * @param <S> Response type
     * @return the instantiated generic application builder
     */
    public static <Q extends Request, S extends Response> ApplicationBuilder<Q, S> builder() {

        return new ApplicationBuilder<>();

    }

    /**
     * Instantiates a basic application builder.
     *
     * @return the instantiated basic application builder
     */
    public static ApplicationBuilder<Request, Response> basic() {

        return builder()
            .exchangeParser(new BasicExchangeParser())
            .exceptionHandler(new ExceptionHandler() {
            });

    }

    private void handleExchange(HttpExchange exchange) {

        threadPool.submit(() -> {

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
    public static class ApplicationBuilder<Q extends Request, S extends Response> {

        private Router<Q, S> router;
        private ExchangeParser<Q> exchangeParser;
        private ExceptionHandler exceptionHandler;
        private final Set<Server> servers = new HashSet<>();
        private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        ApplicationBuilder() {
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

        public ApplicationBuilder<Q, S> port(int port) {

            return server(Server.builder().port(port).build());

        }

        public ApplicationBuilder<Q, S> server(@NonNull Server server) {

            this.servers.add(server);
            return this;

        }

        public ApplicationBuilder<Q, S> threadPool(@NonNull ThreadPoolExecutor threadPool) {

            this.threadPool = threadPool;
            return this;

        }

        public Application<Q, S> build() {

            try {

                return new Application<>(
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
