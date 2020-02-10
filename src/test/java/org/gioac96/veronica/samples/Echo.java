package org.gioac96.veronica.samples;

import java.io.IOException;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.matching.RequestMatcher;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;

public class Echo {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        Route fallback = Route.builder()
            .requestHandler(request -> Response.builder()
                .httpStatus(HttpStatus.OK)
                .body("Try to specify a request body")
                .build()
            )
            .build();


        Router router = new Router(fallback);

        router.getRoutes().add(
            Route.builder()
                .requestMatcher(request -> request.getBody().length() > 0)
                .requestHandler(request -> Response.builder()
                    .httpStatus(HttpStatus.OK)
                    .body(request.getBody())
                    .build()
                )
                .build()
        );

        application.setRouter(router);

        application.start();

    }


}
