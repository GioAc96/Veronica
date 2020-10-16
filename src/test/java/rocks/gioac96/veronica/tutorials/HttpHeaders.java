package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;

public class HttpHeaders {

    public static void main(String[] args) {

        RequestHandler requestHandler = req -> {

            Response response;

            switch (req.getHeaders().getFirst("accept").toLowerCase()) {

                case "application/json":
                    response = Response.builder()
                        .body("{\"message\": \"Hello, world!\"}")
                        .header("Content-Type", "application/json")
                        .provide();

                    break;

                case "text/html":
                    response = Response.builder()
                        .body("<h1>Hello, world!</h1>")
                        .header("Content-Type", "text/html")
                        .provide();

                    break;

                default:
                    response = Response.builder()
                        .body("Hello, world!")
                        .header("Content-Type", "text/plain")
                        .provide();

            }

            return response;
        };

        Router router = Router.builder()
            .defaultRequestHandler(requestHandler)
            .provide();

        int port = 8000;

        Application app = Application.builder()
            .port(port)
            .router(router)
            .provide();

        app.start();


    }

}
