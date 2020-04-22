package rocks.gioac96.veronica.tutorials;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class HttpHeaders {

    public static void main(String[] args) {

        Route<Request, Response> route = Route.builder()
            .requestHandler(req -> {

                switch(req.getHeaders().getFirst("accept").toLowerCase()) {

                    case "application/json":
                        return Response.builder()
                            .body("{\"message\": \"Hello, world!\"}")
                            .header("Content-Type", "application/json")
                            .build();

                    case "text/html":
                        return Response.builder()
                            .body("<h1>Hello, world!</h1>")
                            .header("Content-Type", "text/html")
                            .build();

                    default:
                        return Response.builder()
                            .body("Hello, world!")
                            .header("Content-Type", "text/plain")
                            .build();

                }
            })
            .build();

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(route)
            .build();

        int port = 8000;

        try {

            Application<Request, Response> app = Application.basic()
                .port(port)
                .router(router)
                .build();
            app.start();

        } catch (IOException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }



    }

}
