package rocks.gioac96.veronica.core.common;

import rocks.gioac96.veronica.core.common.http_status.ValidationFailureHttpStatus;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.providers.Provider;

public class CommonHttpStatus {

    private final static Provider<HttpStatus> validationFailure = new ValidationFailureHttpStatus();

    public static HttpStatus validationFailure() {

        return validationFailure.provide();

    }

}
