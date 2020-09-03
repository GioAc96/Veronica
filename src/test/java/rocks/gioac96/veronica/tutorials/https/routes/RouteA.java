package rocks.gioac96.veronica.tutorials.https.routes;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;

public class RouteA extends Route.RouteBuilder {

    @Override
    public void configure() {

        requestHandler(Request -> Response.builder()
            .body("Hello world, in Https!")
            .build());

    }

}
