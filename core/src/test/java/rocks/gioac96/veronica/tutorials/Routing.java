package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.common.CommonRequestMatchers.get;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Route;
import rocks.gioac96.veronica.Router;

public class Routing {

    public static void main(String[] args) {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(get("/home"))
                .requestHandler(req -> Response.builder()
                    .body("This is the homepage")
                    .provide()
                )
                .provide()
            )
            .route(Route.builder()
                .requestMatcher(get("/about"))
                .requestHandler(req -> Response.builder()
                    .body("This is the about page")
                    .provide()
                )
                .provide()
            )
            .defaultRequestHandler(req -> Response.builder()
                .body("We could not find what you're looking for")
                .provide()
            ).provide();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(router)
            .provide();

        app.start();

    }

}
