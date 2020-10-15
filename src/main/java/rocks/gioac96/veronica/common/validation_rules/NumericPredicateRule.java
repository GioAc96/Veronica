package rocks.gioac96.veronica.common.validation_rules;

import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public class NumericPredicateRule extends Builder<ValidationRule> implements BuildsMultipleInstances {

    protected Predicate<Double> predicate;
    protected ValidationFailureReason predicateNotAppliesFailureReason;

    public NumericPredicateRule predicate(@NonNull Predicate<Double> predicate) {

        this.predicate = predicate;
        return this;

    }

    public NumericPredicateRule predicate(@NonNull Provider<Predicate<Double>> predicateProvider) {

        return predicate(predicateProvider.provide());

    }

    public NumericPredicateRule predicateNotAppliesFailureReason(@NonNull ValidationFailureReason predicateNotAppliesFailureReason) {

        this.predicateNotAppliesFailureReason = predicateNotAppliesFailureReason;
        return this;

    }

    public NumericPredicateRule predicateNotAppliesFailureReason(
        @NonNull Provider<ValidationFailureReason> predicateNotAppliesFailureReasonProvider
    ) {

        return predicateNotAppliesFailureReason(predicateNotAppliesFailureReasonProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && predicate != null
            && predicateNotAppliesFailureReason != null;

    }

    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            double value;

            try {

                value = Double.parseDouble(fieldValue);

            } catch (NumberFormatException e) {

                ValidationFailureData failureData = ValidationFailureData.builder()
                    .failureReason(CommonValidationFailureReasons.notNumeric())
                    .fieldName(fieldName)
                    .build();

                throw new ValidationException(e, failureData);

            }

            if (!predicate.test(value)) {

                throw new ValidationException(ValidationFailureData.builder()
                    .failureReason(predicateNotAppliesFailureReason)
                    .fieldName(fieldName)
                    .build());

            }

        };

    }

}
