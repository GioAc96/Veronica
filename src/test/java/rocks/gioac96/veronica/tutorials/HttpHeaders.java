package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class HttpHeaders {

    public static void main(String[] args) {

        Route<Request, Response> route = Route.builder()
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

                return ok(response);
            })
            .build();

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(route)
            .build();

        int port = 8000;

        Application<Request, Response> app = Application.basic()
            .port(port)
            .router(router)
            .build();

        app.start();


    }

}
