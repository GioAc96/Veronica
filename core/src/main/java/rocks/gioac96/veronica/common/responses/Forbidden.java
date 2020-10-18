package rocks.gioac96.veronica.common.responses;

import rocks.gioac96.veronica.HttpStatus;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.providers.Singleton;

public class Forbidden
    extends Response.ResponseBuilder
    implements Singleton {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.FORBIDDEN);
        emptyBody();

        super.configure();

    }

}
