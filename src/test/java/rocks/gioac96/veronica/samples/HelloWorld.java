package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class HelloWorld {

    public static void main(String[] args) throws IOException, CreationException {


        Router router = Router.builder()
            .defaultRoute(
                Route.builder()
                    .requestHandler(request -> Response.builder()
                        .body("Hello, world!")
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
