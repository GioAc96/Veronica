package rocks.gioac96.veronica.samples;


import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Query {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(request -> request.getQueryMap().size() > 0)
                .requestHandler(
                    request -> ok(Response.builder()
                        .body(request.getQueryMap().toString())
                        .build())
                )
                .build()
            )
            .fallbackRoute(Route.builder()
                .requestHandler(request -> ok(Response.builder()
                    .body("Request is empty")
                    .build())
                )
                .build()
            )
            .build();

        Application application = Application.basic()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
