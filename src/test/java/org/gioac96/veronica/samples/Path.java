package org.gioac96.veronica.samples;

import java.io.IOException;
import org.gioac96.veronica.Application;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.Route;
import org.gioac96.veronica.routing.Router;

public class Path {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        application.setRouter(Router.builder()
            .fallbackRoute(Route.builder()
                .requestHandler(request -> Response.builder()
                    .body("The request path is: " + request.getPath())
                    .build()
                )
                .build()
            )
            .build()
        );

        application.start();

    }

}
