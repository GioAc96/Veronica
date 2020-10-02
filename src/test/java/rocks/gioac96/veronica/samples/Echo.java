package rocks.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;

public class Echo {

    @Getter
    private final Router router;

    @Getter
    private final static String errorMessage = "Try to insert something in the request body";

    public Echo() {

        router = Router.builder()
            .defaultRequestHandler(request -> {
                if (request.getBody().length() > 0) {

                    return Response.builder()
                        .body(request.getBody())
                        .build();

                } else {

                    return Response.builder()
                        .body(errorMessage)
                        .build();

                }
            })
            .build();
    }

    public static void main(String[] args) throws IOException, CreationException {

        Echo echo = new Echo();

        Application application = Application.builder()
            .port(80)
            .router(echo.router)
            .build();

        application.start();

    }

}
