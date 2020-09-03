package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class HttpHeaders {

    public static void main(String[] args) {

        Route route = Route.builder()
            .requestHandler(req -> {

                Response response;

                switch (req.getHeaders().getFirst("accept").toLowerCase()) {

                    case "application/json":
                        response = Response.builder()
                            .body("{\"message\": \"Hello, world!\"}")
                            .header("Content-Type", "application/json")
                            .build();

                        break;

                    case "text/html":
                        response = Response.builder()
                            .body("<h1>Hello, world!</h1>")
                            .header("Content-Type", "text/html")
                            .build();

                        break;

                    default:
                        response = Response.builder()
                            .body("Hello, world!")
                            .header("Content-Type", "text/plain")
                            .build();

                }

                return response;
            })
            .build();

        Router router = Router.builder()
            .fallbackRoute(route)
            .build();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(router)
            .build();

        app.start();


    }

}
