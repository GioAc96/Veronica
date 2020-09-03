package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;

public class Echo {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .route(
                Route.builder()
                    .requestMatcher(
                        request -> request.getBody().length() > 0
                    )
                    .requestHandler(
                        request -> Response.builder()
                            .body(request.getBody())
                            .build()
                    )
                    .build()
            )
            .fallbackRoute(
                Route.builder()
                    .requestHandler(
                        request -> Response.builder()
                            .body("Try to insert something in the request body")
                            .build()
                    )
                    .build()
            )
            .build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }


}
