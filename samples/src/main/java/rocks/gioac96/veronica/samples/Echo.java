package rocks.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.routing.Router;

public class Echo {

    @Getter
    private final static String errorMessage = "Try to insert something in the request body";
    @Getter
    private final Router router;

    public Echo() {

        router = Router.builder()
            .defaultRequestHandler(request -> {
                if (request.getBody().length() > 0) {

                    return Response.builder()
                        .body(request.getBody())
                        .provide();

                } else {

                    return Response.builder()
                        .body(errorMessage)
                        .provide();

                }
            })
            .provide();
    }

    public static void main(String[] args) throws IOException, CreationException {

        Echo echo = new Echo();

        Application application = Application.builder()
            .port(80)
            .requestHandler(echo.router)
            .provide();

        application.start();

    }

}
