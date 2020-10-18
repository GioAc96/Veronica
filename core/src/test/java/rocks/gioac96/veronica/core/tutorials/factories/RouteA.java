package rocks.gioac96.veronica.core.tutorials.factories;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.common.CommonRequestMatchers;

public class RouteA extends Route.RouteBuilder {

    @Override
    public void configure() {

        requestMatcher(CommonRequestMatchers.get("/a"));

        requestHandler(request -> Response.builder()
            .body("This is route A")
            .provide()
        );

    }


}
