package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload.ok;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Server;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Path {

    public static void main(String[] args) throws IOException, CreationException {


        Router<Request, Response> router = Router.builder()
            .fallbackRoute(Route.builder()
                .requestHandler(request -> ok(Response.builder()
                    .body("The request path is: " + request.getPath())
                    .build())
                )
                .build()
            )
            .build();

        Application<Request, Response> application = Application.basic()
            .server(Server.builder().port(80).build())
            .router(router)
            .build();

        application.start();

    }

}
