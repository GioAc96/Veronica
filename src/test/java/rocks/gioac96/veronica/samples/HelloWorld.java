package rocks.gioac96.veronica.samples;

import java.io.IOException;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;

public class HelloWorld {

    public static void main(String[] args) throws IOException, CreationException {

        Router router = Router.builder()
            .defaultRequestHandler(request -> Response.builder()
                .body("Hello, world!")
                .build()
            ).build();

        Application application = Application.builder()
            .port(80)
            .router(router)
            .build();

        application.start();

    }

}
