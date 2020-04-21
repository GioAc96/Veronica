package rocks.gioac96.veronica.tutorials;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.PipelineBreakException;
import rocks.gioac96.veronica.routing.pipeline.RequestHandler;

public class HelloWorld {

    public static void main(String[] args) {

        RequestHandler<Request, Response> helloWorldHandler = request -> Response.builder()
            .body("Hello World")
            .build();

        Route<Request, Response> helloWorldRoute = Route.builder()
            .requestHandler(helloWorldHandler)
            .build();

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(helloWorldRoute)
            .build();

        int port = 8000;

        try {

            Application<Request, Response> app = Application.basic(port, router);
            app.start();

        } catch (IOException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }

    }

}