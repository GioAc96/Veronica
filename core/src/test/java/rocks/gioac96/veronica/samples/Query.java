package rocks.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.providers.CreationException;

public class Query {

    @Getter
    private final Router router = Router.builder()
        .defaultRequestHandler(request -> Response.builder()
            .body(request.getQueryMap().toString())
            .provide())
        .provide();

    public static void main(String[] args) throws IOException, CreationException {

        Application application = Application.builder()
            .port(80)
            .router(new Query().getRouter())
            .provide();

        application.start();

    }

}
