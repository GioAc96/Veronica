package rocks.gioac96.veronica.common.http_status;

import rocks.gioac96.veronica.HttpStatus;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;

public class ValidationFailureHttpStatus
    extends ConfigurableProvider<HttpStatus>
    implements Singleton {

    @Override
    protected HttpStatus instantiate() {

        return HttpStatus.UNPROCESSABLE_ENTITY;

    }

}
