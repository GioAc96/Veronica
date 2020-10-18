package rocks.gioac96.veronica.samples;

import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.core.providers.CreationException;

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
