package rocks.gioac96.veronica.tutorials.https.routes;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteA extends RouteFactory<Request, Response> {

    @Override
    public void configure() {

        requestHandler(Request -> ok(Response.builder()
            .body("Hello world, in Https!")
            .build()));

    }

}
