package rocks.gioac96.veronica.common.validation_failure_reasons;

import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NotInRange extends Builder<ValidationFailureReason> implements BuildsMultipleInstances {

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
            .build();

    }

}
