package rocks.gioac96.veronica.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;

public class NotFound extends Response.ResponseBuilder {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.NOT_FOUND);
        emptyBody();

    }

}
