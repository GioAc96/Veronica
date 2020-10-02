package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;

public class Echo {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .defaultRequestHandler(request -> {
                if (request.getBody().length() > 0) {

                    return Response.builder()
                        .body(request.getBody())
                        .build();

                } else {

                    return Response.builder()
                        .body("Try to insert something in the request body")
                        .build();

                }
            })
            .build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }


}
