package rocks.gioac96.veronica.samples;

import java.io.IOException;
import lombok.Getter;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.core.providers.CreationException;

public class ResponseHeaders {

    @Getter
    Router router = Router.builder()
        .defaultRequestHandler(request -> Response.builder()
            .header("content-type", "application/json")
            .body("{\"hello\": \"world\"}")
            .provide())
        .provide();

    public static void main(String[] args) throws IOException, CreationException {


        Application application = Application.builder()
            .port(80)
            .requestHandler(new ResponseHeaders().router)
            .provide();

        application.start();

    }

}
