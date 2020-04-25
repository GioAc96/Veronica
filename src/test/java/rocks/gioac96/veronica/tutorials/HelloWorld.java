package rocks.gioac96.veronica.tutorials;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;

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

            Application<Request, Response> app = Application.basic()
                .port(port)
                .router(router)
                .build();
            app.start();

        } catch (CreationException e) {

            System.out.println("Unable to start the application: " + e.getMessage());

        }

    }

}
