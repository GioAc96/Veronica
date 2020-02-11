package org.gioac96.veronica.routing.pipeline.validation.rules;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;
import org.gioac96.veronica.util.ArraySet;


/**
 * Validation rule that checks that a field's value is among the specified valid values.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedValues;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
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

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        validate(fieldName, fieldValue, CommonValidationFailureReason.NOT_IN_ARRAY);

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class InArrayRuleBuilder {

        private ArraySet<String> allowedValues = new ArraySet<>();

        public InArrayRuleBuilder allowedValues(String... allowedValues) {

            Collections.addAll(this.allowedValues, allowedValues);

            return this;

        }

        public InArrayRuleBuilder allowedValues(Collection<String> allowedValues) {

            this.allowedValues.addAll(allowedValues);

            return this;

        }

        public InArrayRule build() {

            return new InArrayRule(
                allowedValues
            );

        }

    }

}
