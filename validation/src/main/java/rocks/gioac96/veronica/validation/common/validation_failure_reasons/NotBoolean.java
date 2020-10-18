package rocks.gioac96.veronica.validation.common.validation_failure_reasons;

import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Singleton;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NotBoolean
    extends ConfigurableProvider<ValidationFailureReason>
    implements Singleton {

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value must be either true or false")
            .provide();

    }

}
