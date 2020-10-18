package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Route;

public class RouteA extends Route.RouteBuilder  {

    @Override
    public void configure() {

        requestHandler(request -> Response.builder()
            .body("Is secure: " + request.isSecure())
            .provide()
        );

    }

}
