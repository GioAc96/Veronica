package rocks.gioac96.veronica.samples;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.Objects;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.ExceptionHandler;
import rocks.gioac96.veronica.ExchangeParseException;
import rocks.gioac96.veronica.ExchangeParser;
import rocks.gioac96.veronica.HttpMethod;
import rocks.gioac96.veronica.Request;
import rocks.gioac96.veronica.Route;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.static_server.FilePermissionsManager;

public class StaticServerCustomRequest {

    private static final String PASSWORD = "veronica<3";

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .exceptionHandler(new ExceptionHandler() {
            })
            .exchangeParser(AuthenticatedRequest.parser)
            .router(Router.builder()
                .route(Route.<Boolean>staticRouteBuilder()
                    .permissionManager(FilePermissionsManager.<Boolean>builder()
                        .permissions("D:\\projects\\veronica\\src", true)
                        .permissions("D:\\projects\\veronica\\src\\test", false)
                        .provide())
                    .permissionDecider((request, filePermissions) -> AuthenticationService.isAuthenticated(request) && filePermissions)
                    .basePath("")
                    .baseDir("D:\\projects\\veronica\\src")
                    .provide())
                .defaultRequestHandler(CommonRequestHandlers.notFound())
                .provide())
            .provide()
            .start();


    }

    private static class AuthenticatedRequest extends Request {

        private static final ExchangeParser basicParser = new ExchangeParser() {
        };

        private static final ExchangeParser parser = new ExchangeParser() {
            @Override
            public Request parseExchange(HttpExchange httpExchange) throws ExchangeParseException {

                Request request = basicParser.parseExchange(httpExchange);

                return new AuthenticatedRequest(
                    request.getHttpMethod(),
                    request.getBody(),
                    request.getHeaders(),
                    request.getUri(),
                    request.isSecure()
                );

            }
        };

        protected AuthenticatedRequest(
            @NonNull HttpMethod httpMethod,
            @NonNull String body,
            @NonNull Headers headers,
            @NonNull URI uri,
            boolean secure
        ) {

            super(
                httpMethod,
                body,
                headers,
                uri,
                secure
            );

        }

        public boolean isAuthenticated() {

            return Objects.equals(
                getQueryMap().get("password"),
                PASSWORD
            );

        }

    }

    private static class AuthenticationService {

        public static boolean isAuthenticated(Request request) {

            return request instanceof AuthenticatedRequest && ((AuthenticatedRequest) request).isAuthenticated();

        }

    }

}
