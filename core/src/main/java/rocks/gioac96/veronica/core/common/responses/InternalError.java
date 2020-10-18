package rocks.gioac96.veronica.core.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Singleton;

public class InternalError
    extends Response.ResponseBuilder
    implements Singleton {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        emptyBody();

        super.configure();

    }

}
