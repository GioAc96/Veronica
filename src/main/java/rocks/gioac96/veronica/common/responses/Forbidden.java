package rocks.gioac96.veronica.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;

public class Forbidden extends Response.ResponseBuilder {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.FORBIDDEN);
        emptyBody();

    }

}
