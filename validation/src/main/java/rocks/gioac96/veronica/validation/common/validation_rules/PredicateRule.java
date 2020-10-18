package rocks.gioac96.veronica.core.validation.validation_rules;

import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Provider;
import validation.ValidationException;
import validation.ValidationRule;

public class PredicateRule
    extends ValidationRuleBuilderWithConstantFailureReason {

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

        return fieldValue -> {

            if (!predicate.test(fieldValue)) {

                throw new ValidationException(failureReason);

            }

        };

    }

}
