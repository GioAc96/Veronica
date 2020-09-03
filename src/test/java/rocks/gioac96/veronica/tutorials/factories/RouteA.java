package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.RouteFactory;

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
