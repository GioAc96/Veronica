package rocks.gioac96.veronica.tutorials.https.handlers;

import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;

public class MyHandler extends ConfigurableProvider<RequestHandler>  {

    @Override
    public RequestHandler instantiate() {

        return request -> Response.builder()
            .body("Hello world, in Https!")
            .provide();

    }

}
