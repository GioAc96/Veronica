package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;

public class RouteB extends Route.RouteBuilder  {

    @Override
    public void configure() {

        requestMatcher(get("/b"));

        requestHandler(request -> Response.builder()
            .body("And this is route B")
            .provide()
        );

    }


}
