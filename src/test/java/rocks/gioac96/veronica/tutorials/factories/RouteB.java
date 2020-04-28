package rocks.gioac96.veronica.tutorials.factories;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.get;
import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.RouteFactory;

public class RouteB extends RouteFactory<Request, Response> {

    @Override
    public void configure() {

        requestMatcher(get("/b"));

        requestHandler(request -> ok(Response.builder()
            .body("And is route B")
            .build())
        );

    }

}
