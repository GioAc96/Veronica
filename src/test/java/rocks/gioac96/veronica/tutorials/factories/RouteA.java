package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;
import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteA extends RouteFactory<Request, Response> {

    @Override
    public void configure() {

        requestMatcher(get("/a"));

        handler(request -> Response.builder()
            .body("This is route A")
            .build()
        );

    }


}
