package rocks.gioac96.veronica.validation.common.validation_failure_reasons;

import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class ValueTooBig extends ConfigurableProvider<ValidationFailureReason> {

    protected Double maxValue;

    public ValueTooBig maxValue(@NonNull Double maxValue) {

        this.maxValue = maxValue;
        return this;

    }

    public ValueTooBig maxValue(@NonNull Provider<Double> maxValueProvider) {

        return maxValue(maxValueProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && maxValue != null;

    }

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value must be smaller than " + maxValue)
            .provide();

    }

}
