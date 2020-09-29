package rocks.gioac96.veronica.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class Ok extends Response.ResponseBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        httpStatus(HttpStatus.OK);
        emptyBody();

    }

}
