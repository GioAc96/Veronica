package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;

import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteB extends RouteFactory {

    @Override
    public void configure() {

        requestMatcher(get("/b"));

        handler(request -> Response.builder()
            .body("And this is route B")
            .build()
        );

    }

}
