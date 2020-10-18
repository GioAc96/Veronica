package rocks.gioac96.veronica.common.validation_failure_reasons;

import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Singleton;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NotNumeric
    extends ConfigurableProvider<ValidationFailureReason>
    implements Singleton {

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value is not a valid number")
            .provide();

    }

}