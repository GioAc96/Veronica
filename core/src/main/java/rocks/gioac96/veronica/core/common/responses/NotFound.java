package rocks.gioac96.veronica.core.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Singleton;

public class NotFound
    extends Response.ResponseBuilder
    implements Singleton {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.NOT_FOUND);
        emptyBody();

        super.configure();

    }

}