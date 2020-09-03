package rocks.gioac96.veronica.tutorials.multiple_servers;

import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteA extends RouteFactory {

    @Override
    public void configure() {

        requestHandler(request -> Response.builder()
            .body("Is secure: " + request.isSecure())
            .build()
        );

    }

}
