package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Veronica application.
 */
@SuppressWarnings("unused")
public final class Application {

    private final Set<HttpServer> httpServers;
    private final RequestHandler requestHandler;
    private final ExchangeParser exchangeParser;
    private final ExceptionHandler exceptionHandler;

    protected Application(
        ApplicationBuilder b
    ) throws IOException {

        this.requestHandler = b.requestHandler;
        this.exchangeParser = b.exchangeParser;
        this.exceptionHandler = b.exceptionHandler;

        this.httpServers = b.httpServers;

        this.httpServers.forEach(httpServer -> {

            httpServer.createContext("/", this::handleExchange);

        });

    }

    /**
     * Instantiates a generic application builder.
     *
     * @return the instantiated generic application builder
     */
    public static ApplicationBuilder builder() {

        class ApplicationBuilderImpl extends ApplicationBuilder implements BuildsMultipleInstances {

        }

        return new ApplicationBuilderImpl();


    }

    private void handleExchange(HttpExchange exchange) {

        Response response;

        try {

            // Parse request
            Request request = exchangeParser.parseExchange(exchange);

            // Generate response
            response = requestHandler.handle(request);


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
    public abstract static class ApplicationBuilder extends Builder<Application> {

        private final Set<HttpServer> httpServers = new HashSet<>();
        private RequestHandler requestHandler;

        private ExchangeParser exchangeParser = new ExchangeParser() {
        };

        private ExceptionHandler exceptionHandler = new ExceptionHandler() {
        };

        public ApplicationBuilder router(@NonNull Router router) {

            this.requestHandler = router;
            return this;

        }

        public ApplicationBuilder router(@NonNull Provider<Router> routerProvider) {

            return router(routerProvider.provide());

        }

        public ApplicationBuilder requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return this;

        }


        public ApplicationBuilder requestHandler(@NonNull Provider<RequestHandler> requestHandler) {

            return requestHandler(requestHandler.provide());

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

            return server(new ServerBuilder().port(port).build());

        }

        public ApplicationBuilder port(@NonNull Provider<Integer> port) {

            return server(new ServerBuilder().port(port.provide()).build());

        }

        public ApplicationBuilder server(@NonNull HttpServer server) {

            this.httpServers.add(server);
            return this;

        }

        public ApplicationBuilder server(@NonNull Provider<HttpServer> serverProvider) {

            return server(serverProvider.provide());

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && httpServers != null
                && httpServers.stream().allMatch(Objects::nonNull)
                && requestHandler != null
                && exchangeParser != null
                && exceptionHandler != null;


        }

        @Override
        protected Application instantiate() {

            try {

                return new Application(this);

            } catch (IOException e) {

                throw new CreationException(e);

            }

        }

    }

}
