package rocks.gioac96.veronica.validation.common.http_status;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Singleton;

public class ValidationFailureHttpStatus
    extends ConfigurableProvider<HttpStatus>
    implements Singleton {

    @Override
    protected HttpStatus instantiate() {

        return HttpStatus.UNPROCESSABLE_ENTITY;

    }

}
