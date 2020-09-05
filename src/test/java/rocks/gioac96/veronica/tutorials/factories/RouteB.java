package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class RouteB extends Route.RouteBuilder implements BuildsSingleInstance {

    @Override
    public void configure() {

        requestMatcher(get("/b"));

        requestHandler(request -> Response.builder()
            .body("And this is route B")
            .build()
        );

    }


}
