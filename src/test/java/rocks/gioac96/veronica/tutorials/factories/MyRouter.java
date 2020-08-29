package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.http.CommonResponses;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.CommonRoutes;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.RouterFactory;

public class MyRouter extends RouterFactory<Request, Response> {

    @Override
    public void configure() {

        route(new RouteA());
        route(new RouteB());

        fallbackRoute(CommonRoutes.notFound());

    }


}
