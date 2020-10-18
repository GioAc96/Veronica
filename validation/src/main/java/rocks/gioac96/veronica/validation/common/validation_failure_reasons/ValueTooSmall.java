package rocks.gioac96.veronica.validation.common.validation_failure_reasons;

import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class ValueTooSmall extends ConfigurableProvider<ValidationFailureReason> {

    protected Double minValue;

    public ValueTooSmall minValue(@NonNull Double minValue) {

        this.minValue = minValue;
        return this;

    }

    public ValueTooSmall minValue(@NonNull Provider<Double> minValueProvider) {

        return minValue(minValueProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && minValue != null;

    }

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value must be greater than " + minValue)
            .provide();

    }

}
