package rocks.gioac96.veronica.core.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;

public class RouteA extends Route.RouteBuilder {

    @Override
    public void configure() {

        requestHandler(request -> Response.builder()
            .body("Is secure: " + request.isSecure())
            .provide()
        );

    }

}
