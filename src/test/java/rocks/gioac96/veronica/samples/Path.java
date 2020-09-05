package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;

public class Path {

    public static void main(String[] args) throws CreationException {

        Router router = Router.builder()
            .defaultRoute(Route.builder()
                .requestHandler(request -> Response.builder()
                    .body("The request path is: " + request.getPath())
                    .build()
                )
                .build()
            )
            .build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
