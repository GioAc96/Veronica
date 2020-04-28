package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.routing.matching.CommonRequestMatchers.path;
import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Routing {

    public static void main(String[] args) {

        Router<Request, Response> router = Router.builder()
            .route(Route.builder()
                .requestMatcher(path("/home"))
                .requestHandler(req -> ok(Response.builder()
                    .body("This is the homepage")
                    .build())
                )
                .build()
            )
            .route(Route.builder()
                .requestMatcher(path("/about"))
                .requestHandler(req -> ok(Response.builder()
                    .body("This is the about page")
                    .build())
                )
                .build()
            )
            .fallbackRoute(Route.builder()
                .requestHandler(req -> ok(Response.builder()
                    .body("We could not find what you're looking for")
                    .build())
                )
                .build()
            )
            .build();

        int port = 8000;

        Application<Request, Response> app = Application.basic()
            .port(port)
            .router(router)
            .build();

        app.start();


    }

}
