package org.gioac96.veronica.samples;

import java.io.IOException;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;

public class HelloWorld {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        application.setRouter(
            Router.builder()
                .fallbackRoute(
                    Route.builder()
                        .requestHandler(request -> Response.builder()
                            .body("Hello, world!")
                            .build()
                        )
                        .build()
                )
                .build()
        );

        application.start();

    }

}
