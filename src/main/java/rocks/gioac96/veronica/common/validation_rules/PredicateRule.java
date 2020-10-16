package rocks.gioac96.veronica.common.validation_rules;

import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class PredicateRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsMultipleInstances {

    protected Predicate<String> predicate;

    public PredicateRule predicate(@NonNull Predicate<String> predicate) {

        this.predicate = predicate;
        return this;

    }

    public PredicateRule predicate(@NonNull Provider<Predicate<String>> predicateProvider) {

        return predicate(predicateProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && predicate != null;

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            if (! predicate.test(fieldValue)) {

                throw new ValidationException(
                    ValidationFailureData.builder()
                        .fieldName(fieldName)
                        .failureReason(failureReason)
                        .build()
                );

            }

        };

    }

}
