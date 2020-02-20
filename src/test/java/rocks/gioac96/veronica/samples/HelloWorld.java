package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class HelloWorld {

    public static void main(String[] args) throws IOException {


        Router<Request, Response> router = Router.builder()
                .fallbackRoute(
                    Route.builder()
                        .requestHandler(request -> Response.builder()
                            .body("Hello, world!")
                            .build()
                        )
                        .build()
                )
                .build();

        Application<Request, Response> application = Application.basic(80, router);

        application.start();

    }

}
