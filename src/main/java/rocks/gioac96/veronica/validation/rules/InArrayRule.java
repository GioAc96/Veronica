package rocks.gioac96.veronica.validation.rules;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.validation.ValidationRule;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Validation rule that checks that a field's value is among the specified valid values.
 */
@SuppressWarnings("unused")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedValues;

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
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
    public static class InArrayRuleBuilder {

        private final ArraySet<String> allowedValues = new ArraySet<>();

        @SuppressWarnings("unused")
        public InArrayRuleBuilder allowedValue(String allowedValue) {

            this.allowedValues.add(allowedValue);

            return this;

        }

        @SuppressWarnings("unused")
        public InArrayRuleBuilder allowedValues(Collection<String> allowedValues) {

            this.allowedValues.addAll(allowedValues);

            return this;

        }

        @SuppressWarnings("unused")
        public InArrayRule build() {

            return new InArrayRule(
                allowedValues
            );

        }

    }

}
