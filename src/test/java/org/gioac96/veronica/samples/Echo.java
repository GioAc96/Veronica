package org.gioac96.veronica.samples;

import java.io.IOException;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.RequestMatcher;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;
import org.gioac96.veronica.routing.pipeline.RequestHandler;

public class Echo {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        Route fallback = new Route(
            request -> Response.builder()
                .httpStatus(HttpStatus.OK)
                .body("Make a request with a non-empty body to hear your echo!")
                .build()
        );

        Route echo = new Route(
            request -> request.getBody().length() > 0,
            request -> Response.builder()
                .httpStatus(HttpStatus.OK)
                .body(request.getBody())
                .build()
        );

        Router router = new Router(fallback);

        router.getRoutes().add(echo, 0);

        application.setRouter(router);

        application.start();

    }

}
