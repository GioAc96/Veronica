package rocks.gioac96.veronica.tutorials.https.handlers;

import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.providers.Provider;

public class MyHandler extends Builder<RequestHandler> implements BuildsSingleInstance {

    @Override
    public RequestHandler instantiate() {

        return request -> Response.builder()
            .body("Hello world, in Https!")
            .build();

    }

}
