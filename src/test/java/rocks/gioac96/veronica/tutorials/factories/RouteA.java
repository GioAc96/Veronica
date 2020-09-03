package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;

public class RouteA extends Route.RouteBuilder {

    @Override
    public void configure() {

        requestMatcher(get("/a"));

        requestHandler(request -> Response.builder()
            .body("This is route A")
            .build()
        );

    }


}
