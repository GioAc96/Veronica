package rocks.gioac96.veronica.common.validation_rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class InArray extends ValidationRuleBuilderWithConstantFailureReason {

    protected Set<String> allowedValues = new HashSet<>();

    public InArray allowedValue(@NonNull String allowedValue) {

        this.allowedValues.add(allowedValue);
        return this;

    }

    public InArray allowedValue(@NonNull Provider<String> allowedValueProvider) {

        return allowedValue(allowedValueProvider.provide());

    }

    public InArray allowedValues(@NonNull Collection<String> allowedValues) {

        this.allowedValues.addAll(allowedValues);

        return this;

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && allowedValues != null
            && !allowedValues.isEmpty()
            && allowedValues.stream().allMatch(Objects::nonNull);

    }

    @Override
    protected void configure() {

        if (failureReason == null) {

            failureReason(CommonValidationFailureReasons.notInArray());

        }

        super.configure();

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            if (allowedValues.stream().noneMatch(allowedValues -> allowedValues.equals(fieldValue))) {

                ValidationFailureData failureData = ValidationFailureData.builder()
                    .failureReason(failureReason)
                    .fieldName(fieldName)
                    .provide();


                throw new ValidationException(failureData);

            }

        };

    }

}
