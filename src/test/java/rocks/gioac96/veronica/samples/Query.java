package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.RequestMatcher;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;

public class Query {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(RequestMatcher.builder()
                    .condition(request -> request.getQueryMap().size() > 0)
                    .build())
                .requestHandler(request -> Response.builder()
                    .body(request.getQueryMap().toString())
                    .build())
                .build())
            .defaultRequestHandler(request -> Response.builder()
                .body("Request query is empty")
                .build())
            .build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
