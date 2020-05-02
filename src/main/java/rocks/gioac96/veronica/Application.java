package rocks.gioac96.veronica;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
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
        int threads
    ) throws IOException {

        this.exchangeParser = exchangeParser;
        this.router = router;
        this.exceptionHandler = exceptionHandler;

        validateThreads(threads);
        this.threadPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(threads);

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

        return new ApplicationBuilder<>()
            .exchangeParser(new BasicExchangeParser())
            .exceptionHandler(new ExceptionHandler() {
            });

    }

    private static void validateThreads(int threads) {

        if (threads < 1) {

            throw new IllegalArgumentException("Threads count must be >= 1");

        }

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
        private int threads = Runtime.getRuntime().availableProcessors();

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

        public ApplicationBuilder<Q, S> server(Server server) {

            this.servers.add(server);
            return this;

        }

        public ApplicationBuilder<Q, S> threads(int threads) {

            validateThreads(threads);

            this.threads = threads;
            return this;

        }

        public Application<Q, S> build() {

            try {

                return new Application<>(servers, router, exchangeParser, exceptionHandler, threads);

            } catch (IOException e) {

                throw new CreationException(e);

            }

        }

    }

}
