package rocks.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.core.Response;

public class ResponseHeaders {

    @Getter
    Router router = Router.builder()
        .defaultRequestHandler(request -> Response.builder()
            .header("content-type", "application/json")
            .body("{\"hello\": \"world\"}")
            .build())
        .build();

    public static void main(String[] args) throws IOException, CreationException {


        Application application = Application.builder()
            .port(80)
            .router(new ResponseHeaders().router)
            .build();

        application.start();

    }

}
