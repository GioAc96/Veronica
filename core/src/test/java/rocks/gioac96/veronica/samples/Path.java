package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.providers.CreationException;

public class Path {

    public static final String message = "The request path is: ";

    @Getter
    private final Router router = Router.builder()
        .defaultRequestHandler(request -> Response.builder()
            .body(message + request.getPath())
            .provide()
        ).provide();

    public static void main(String[] args) throws CreationException {

        Application application = Application.builder()
            .port(80)
            .router(new Path().router)
            .provide();

        application.start();

    }

}
