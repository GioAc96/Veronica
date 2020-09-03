package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;

import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteA extends RouteFactory {

    @Override
    public void configure() {

        requestMatcher(get("/a"));

        handler(request -> Response.builder()
            .body("This is route A")
            .build()
        );

    }


}
