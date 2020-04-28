package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.RouterFactory;

public class MyRouter extends RouterFactory<Request, Response> {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        fallbackRoute(Route.builder()
            .requestHandler(request -> ok(Response.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .body("Not found")
                .build()
            ))
            .build()
        );

    }


}
