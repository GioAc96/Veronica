package rocks.gioac96.veronica.tutorials.https.handlers;

import rocks.gioac96.veronica.RequestHandler;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.providers.ConfigurableProvider;

public class MyHandler extends ConfigurableProvider<RequestHandler> {

    @Override
    public RequestHandler instantiate() {

        return request -> Response.builder()
            .body("Hello world, in Https!")
            .provide();

    }

}
