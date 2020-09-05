package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.path;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class Routing {

    public static void main(String[] args) {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(path("/home"))
                .requestHandler(req -> Response.builder()
                    .body("This is the homepage")
                    .build()
                )
                .build()
            )
            .route(Route.builder()
                .requestMatcher(path("/about"))
                .requestHandler(req -> Response.builder()
                    .body("This is the about page")
                    .build()
                )
                .build()
            )
            .defaultRoute(Route.builder()
                .requestHandler(req -> Response.builder()
                    .body("We could not find what you're looking for")
                    .build()
                )
                .build()
            )
            .build();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(router)
            .build();

        app.start();

    }

}
