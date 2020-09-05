package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class RouteA extends Route.RouteBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        requestHandler(request -> Response.builder()
            .body("Is secure: " + request.isSecure())
            .build()
        );

    }

}
