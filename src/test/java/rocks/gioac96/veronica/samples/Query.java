package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class Query {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(request -> request.getQueryMap().size() > 0)
                .requestHandler(
                    request -> Response.builder()
                        .body(request.getQueryMap().toString())
                        .build()
                )
                .build()
            )
            .fallbackRoute(Route.builder()
                .requestHandler(request -> Response.builder()
                    .body("Request is empty")
                    .build()
                )
                .build()
            )
            .build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
