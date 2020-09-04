package rocks.gioac96.veronica.validation.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.util.ArraySet;
import rocks.gioac96.veronica.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.validation.ValidationRule;

/**
 * Validation rule that checks that a field's value is among the specified valid values.
 */
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedValues;

    public InArrayRule(@NonNull Collection<String> allowedValues) {

        this.allowedValues = new ArraySet<>();

        this.allowedValues.addAll(allowedValues);

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod"})
    public static InArrayRuleBuilder builder() {

        return new InArrayRuleBuilder();

    }

    protected void validate(
        String fieldName,
        String fieldValue,
        ValidationFailureReason failureReason
    ) throws ValidationException {

        if (allowedValues.none(allowedValue -> allowedValue.equals(fieldValue))) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .failureReason(failureReason)
                .fieldName(fieldName)
                .build();

            ValidationFailureResponse failureResponse = ValidationFailureResponse.builder()
                .validationFailureData(failureData)
                .build();

            throw new ValidationException(failureResponse, failureData);

        }

    }

    @SuppressWarnings("unused")
    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        validate(fieldName, fieldValue, CommonValidationFailureReason.NOT_IN_ARRAY);

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class InArrayRuleBuilder extends Builder<InArrayRule> {

        private final Set<String> allowedValues = new HashSet<>();

        public InArrayRuleBuilder allowedValue(String allowedValue) {

            this.allowedValues.add(allowedValue);

            return this;

        }

        public InArrayRuleBuilder allowedValues(Collection<String> allowedValues) {

            this.allowedValues.addAll(allowedValues);

            return this;

        }

        @Override
        protected InArrayRule instantiate() {

            return new InArrayRule(allowedValues);

        }

    }

}
