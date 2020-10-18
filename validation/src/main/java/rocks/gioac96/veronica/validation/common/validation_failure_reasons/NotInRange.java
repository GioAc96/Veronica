package rocks.gioac96.veronica.validation.common.validation_failure_reasons;

import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NotInRange extends ConfigurableProvider<ValidationFailureReason> {

    protected Double minValue;
    protected Double maxValue;

    public NotInRange minValue(@NonNull Double minValue) {

        this.minValue = minValue;
        return this;

    }

    public NotInRange minValue(@NonNull Provider<Double> minValueProvider) {

        return minValue(minValueProvider.provide());

    }


    public NotInRange maxValue(@NonNull Double maxValue) {

        this.maxValue = maxValue;
        return this;

    }

    public NotInRange maxValue(@NonNull Provider<Double> maxValueProvider) {

        return maxValue(maxValueProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && minValue != null
            && maxValue != null
            && minValue < maxValue;

    }

    @Override
    protected ValidationFailureReason instantiate() {

        return ValidationFailureReason.builder()
            .message("value must be between " + minValue + " and " + maxValue)
            .provide();

    }

}
