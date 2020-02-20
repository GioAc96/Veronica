package rocks.gioac96.veronica.samples;

import com.sun.net.httpserver.Headers;
import com.sun.tools.javac.util.List;
import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class ResponseHeaders {

    public static void main(String[] args) throws IOException {


        Headers jsonResponseHeaders = new Headers();

        jsonResponseHeaders.put("content-type", List.of("application/json"));

        Router<Request, Response> router = Router.builder()
            .fallbackRoute(Route.builder()
                .requestHandler(request -> Response.builder()
                    .body("{\"hello\": \"world\"}")
                    .headers(jsonResponseHeaders)
                    .build())
                .build())
            .build();

        Application<Request, Response> application = Application.basic(80, router);

        application.start();

    }

}
