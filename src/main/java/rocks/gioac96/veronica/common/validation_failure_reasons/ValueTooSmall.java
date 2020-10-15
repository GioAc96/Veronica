package rocks.gioac96.veronica.common.validation_failure_reasons;

import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class ValueTooSmall extends Builder<ValidationFailureReason> implements BuildsMultipleInstances {

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
            .build();

    }

}
