package rocks.gioac96.veronica.common.request_handlers;

import rocks.gioac96.veronica.HttpStatus;
import rocks.gioac96.veronica.RequestHandler;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;

public class NotFound
    extends ConfigurableProvider<RequestHandler>
    implements Singleton {

    @Override
    protected RequestHandler instantiate() {

        return request -> Response.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .emptyBody()
            .provide();

    }

}
