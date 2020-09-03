package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Multithreading {

    public static void main(String[] args) {

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(Router.builder()
                .fallbackRoute(Route.builder()
                    .requestHandler(request -> {

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ignored){}

                        return Response.builder()
                            .body("Hello, there!")
                            .build();

                    })
                    .build())
                .build()
            ).build();

        app.start();

    }

}
