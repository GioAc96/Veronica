package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Query {

    public static void main(String[] args) throws IOException, CreationException {


        Router<Request, Response> router = Router.builder()
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

        Application<Request, Response> application = Application.basic()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
