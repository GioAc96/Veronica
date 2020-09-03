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
import rocks.gioac96.veronica.core.ExceptionHandler;
import rocks.gioac96.veronica.core.ExchangeParser;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.Server;
import rocks.gioac96.veronica.core.SetCookieHeader;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Veronica application.
 */
@SuppressWarnings("unused")
public final class Application {

    private final ThreadPoolExecutor threadPool;
    private final Set<HttpServer> httpServers;
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
    public static ApplicationBuilder builder() {

        return new ApplicationBuilder();

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
    public static class ApplicationBuilder extends Builder<Application> {

        private final Set<Server> servers = new HashSet<>();
        private Router router;
        private ExchangeParser exchangeParser = new ExchangeParser() {
        };
        private ExceptionHandler exceptionHandler = new ExceptionHandler() {
        };
        private ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        public ApplicationBuilder router(@NonNull Router router) {

            this.router = router;
            return this;

        }

        public ApplicationBuilder router(@NonNull Provider<Router> routerProvider) {

            return router(routerProvider.provide());

        }

        public ApplicationBuilder exchangeParser(@NonNull ExchangeParser exchangeParser) {

            this.exchangeParser = exchangeParser;
            return this;

        }

        public ApplicationBuilder exchangeParser(@NonNull Provider<ExchangeParser> exchangeParserProvider) {

            return exchangeParser(exchangeParserProvider.provide());

        }

        public ApplicationBuilder exceptionHandler(@NonNull ExceptionHandler exceptionHandler) {

            this.exceptionHandler = exceptionHandler;
            return this;

        }

        public ApplicationBuilder exceptionHandler(@NonNull Provider<ExceptionHandler> exceptionHandlerProvider) {

            return exceptionHandler(exceptionHandlerProvider.provide());

        }

        public ApplicationBuilder port(int port) {

            return server(Server.builder().port(port).build());

        }

        public ApplicationBuilder port(@NonNull Provider<Integer> portProvider) {

            return server(Server.builder().port(portProvider.provide()).build());

        }

        public ApplicationBuilder server(@NonNull Server server) {

            this.servers.add(server);
            return this;

        }

        public ApplicationBuilder server(@NonNull Provider<Server> serverProvider) {

            return server(serverProvider.provide());

        }

        public ApplicationBuilder threadPool(@NonNull ThreadPoolExecutor threadPool) {

            this.threadPool = threadPool;
            return this;

        }

        @Override
        protected Application instantiate() {

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
