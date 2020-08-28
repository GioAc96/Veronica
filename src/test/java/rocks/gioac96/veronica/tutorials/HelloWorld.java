package rocks.gioac96.veronica.tutorials;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Server;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;

public class HelloWorld {

    public static void main(String[] args) {

        RequestHandler<Request, Response> helloWorldHandler = request -> ok(Response.builder()
            .body("Hello World")
            .build());

        Route<Request, Response> helloWorldRoute = Route.builder()
            .requestHandler(helloWorldHandler)
            .build();

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(helloWorldRoute)
            .build();

        int port = 8000;

        Application<Request, Response> app = Application.basic()
            .server(Server.builder()
                .port(port)
                .build()
            ).router(router)
            .build();

        app.start();


    }

}
