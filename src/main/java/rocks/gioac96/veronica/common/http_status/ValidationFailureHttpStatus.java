package rocks.gioac96.veronica.common.http_status;

import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;

public class ValidationFailureHttpStatus extends Builder<HttpStatus> implements BuildsSingleInstance {

    @Override
    protected HttpStatus instantiate() {

        return HttpStatus.UNPROCESSABLE_ENTITY;

    }

}
