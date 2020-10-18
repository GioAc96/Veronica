package rocks.gioac96.veronica.common.request_handlers;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
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
