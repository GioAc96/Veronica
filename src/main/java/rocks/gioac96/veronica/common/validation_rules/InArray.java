package rocks.gioac96.veronica.common.validation_rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public class InArray
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsMultipleInstances {

    private final Set<String> allowedValues = new HashSet<>();

    public InArray allowedValue(String allowedValue) {

        this.allowedValues.add(allowedValue);
        return this;

    }

    public InArray allowedValues(Collection<String> allowedValues) {

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

        super.configure();

        super.failureReason(ValidationFailureReason.builder()
            .message("value is not in array of possible values")
            .build());

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            if (allowedValues.stream().noneMatch(allowedValues -> allowedValues.equals(fieldValue))) {

                ValidationFailureData failureData = ValidationFailureData.builder()
                    .failureReason(failureReason)
                    .fieldName(fieldName)
                    .build();


                throw new ValidationException(failureData);

            }

        };

    }

}
