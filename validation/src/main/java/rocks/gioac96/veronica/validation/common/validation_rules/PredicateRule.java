package rocks.gioac96.veronica.validation.common.validation_rules;

import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationRule;

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
