package rocks.gioac96.veronica.core.common.responses;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Singleton;

public class BadRequest
    extends ConfigurableProvider<Response>
    implements Singleton {

    @Override
    protected Response instantiate() {

        return Response.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .provide();

    }

}
