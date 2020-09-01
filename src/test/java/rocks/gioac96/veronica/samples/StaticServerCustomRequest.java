package rocks.gioac96.veronica.samples;

import com.sun.net.httpserver.Headers;
import java.net.URI;
import java.util.Objects;
import lombok.NonNull;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.BasicExchangeParser;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.HttpMethod;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.CommonRoutes;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.static_server.FilePermissionsManager;

public class StaticServerCustomRequest {

    private static final String PASSWORD = "veronica<3";

    private static class AuthenticatedRequest extends Request {

        private static final ExchangeParser<AuthenticatedRequest> parser = httpExchange -> {

            Request request = BasicExchangeParser.getInstance().parseExchange(httpExchange);

            return new AuthenticatedRequest(
                request.getHttpMethod(),
                request.getBody(),
                request.getHeaders(),
                request.getUri(),
                request.isSecure()
            );

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

    public static void main(String[] args) {

        Application.<AuthenticatedRequest, Response>builder()
            .port(80)
            .exceptionHandler(new ExceptionHandler() {})
            .exchangeParser(AuthenticatedRequest.parser)
            .router(Router.<AuthenticatedRequest, Response>builder()
                .route(Route.<AuthenticatedRequest, Boolean>staticRouteBuilder()
                    .permissionManager(FilePermissionsManager.<Boolean>builder()
                        .setPermissions("D:\\projects\\veronica\\src", true)
                        .setPermissions("D:\\projects\\veronica\\src\\test", false)
                        .build())
                    .permissionDecider((request, filePermissions) -> request.isAuthenticated() && filePermissions)
                    .basePath("")
                    .baseDir("D:\\projects\\veronica\\src")
                    .build())
                .fallbackRoute(CommonRoutes.notFound())
                .build())
            .build()
            .start();


    }

}
