package rocks.gioac96.veronica.core.tutorials;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.common.CommonRequestMatchers;

public class Routing {

    public static void main(String[] args) {

        Router router = Router.builder()
            .route(Route.builder()
                .requestMatcher(CommonRequestMatchers.get("/home"))
                .requestHandler(req -> Response.builder()
                    .body("This is the homepage")
                    .provide()
                )
                .provide()
            )
            .route(Route.builder()
                .requestMatcher(CommonRequestMatchers.get("/about"))
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
