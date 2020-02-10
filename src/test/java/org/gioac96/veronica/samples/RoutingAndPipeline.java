package org.gioac96.veronica.samples;

import static org.gioac96.veronica.routing.matching.CommonRequestMatchers.get;

import java.io.IOException;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;
import org.gioac96.veronica.routing.pipeline.Pipeline;

public class RoutingAndPipeline {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        Route notFound = Route.builder()
            .requestHandler(request -> Response.builder()
                .body("Not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build()
            )
            .build();

        Router router = Router.builder()
            .fallbackRoute(notFound)
            .build();

        router.getRoutes().add(
            Route.builder()
                .requestMatcher(get("/hello"))
                .requestHandler(request -> Response.builder()
                    .httpStatus(HttpStatus.OK)
                    .body("Hello to you, " + request.getQueryParam("name"))
                    .build()
                )
                .pipeline(
                    Pipeline.builder().build()
                )
                .build()
        );


    }

}
