package rocks.gioac96.veronica.validation.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Singleton;

public class Ok
    extends Response.ResponseBuilder
    implements Singleton {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.OK);
        emptyBody();

        super.configure();

    }

}
