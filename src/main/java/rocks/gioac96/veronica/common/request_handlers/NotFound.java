package rocks.gioac96.veronica.common.request_handlers;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class NotFound extends Builder<RequestHandler> implements BuildsSingleInstance {

    @Override
    protected RequestHandler instantiate() {

        return request -> Response.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .emptyBody()
            .build();

    }

}
