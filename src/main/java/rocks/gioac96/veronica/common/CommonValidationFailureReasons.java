package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.validation_failure_reasons.IsNull;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class CommonValidationFailureReasons {

    private static final Provider<ValidationFailureReason> isNull = new IsNull();

    public static ValidationFailureReason isNull() {

        return isNull.provide();

    }

}
