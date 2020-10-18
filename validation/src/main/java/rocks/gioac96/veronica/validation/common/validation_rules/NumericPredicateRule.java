package rocks.gioac96.veronica.validation.common.validation_rules;

import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;

public class NumericPredicateRule extends ConfigurableProvider<ValidationRule> {

    protected Predicate<Double> predicate;
    protected ValidationFailureReason predicateNotAppliesFailureReason;
    protected ValidationFailureReason notNumericFailureReason = CommonValidationFailureReasons.notNumeric();

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

    public NumericPredicateRule notNumericFailureReason(@NonNull ValidationFailureReason notNumericFailureReason) {

        this.notNumericFailureReason = notNumericFailureReason;
        return this;

    }

    public NumericPredicateRule notNumericFailureReason(
        @NonNull Provider<ValidationFailureReason> notNumericFailureReasonProvider
    ) {

        return notNumericFailureReason(notNumericFailureReasonProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && predicate != null
            && predicateNotAppliesFailureReason != null
            && notNumericFailureReason != null;

    }

    protected ValidationRule instantiate() {

        return fieldValue -> {

            double value;

            try {

                value = Double.parseDouble(fieldValue);

            } catch (NumberFormatException e) {

                throw new ValidationException(e, notNumericFailureReason);

            }

            if (!predicate.test(value)) {

                throw new ValidationException(predicateNotAppliesFailureReason);

            }

        };

    }

}
