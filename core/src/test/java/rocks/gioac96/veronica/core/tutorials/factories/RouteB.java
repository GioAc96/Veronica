package rocks.gioac96.veronica.core.tutorials.factories;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.common.CommonRequestMatchers;

public class RouteB extends Route.RouteBuilder {

    @Override
    public void configure() {

        requestMatcher(CommonRequestMatchers.get("/b"));

        requestHandler(request -> Response.builder()
            .body("And this is route B")
            .provide()
        );

    }


}
