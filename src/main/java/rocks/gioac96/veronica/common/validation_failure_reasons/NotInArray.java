package rocks.gioac96.veronica.common.validation_failure_reasons;

import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NotInArray extends Builder<ValidationFailureReason> implements BuildsSingleInstance {

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value is not in array of possible values")
            .build();

    }

}
