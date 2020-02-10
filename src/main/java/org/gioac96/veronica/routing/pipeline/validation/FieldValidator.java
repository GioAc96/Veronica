package org.gioac96.veronica.routing.pipeline.validation;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Field validator.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldValidator {

    @Getter
    @NonNull
    private PrioritySet<ValidationRule> validationRules;

    @Getter
    @Setter
    private boolean nullable;

    /**
     * Validates a field by enforcing all of the field's validation rules.
     *
     * @param fieldName  name of the field to validate
     * @param fieldValue value of the field to validate
     * @throws ValidationException on validation failure
     */
    public void validateField(String fieldName, String fieldValue) throws ValidationException {

        if (fieldValue == null || fieldValue.equals("")) {

            if (!nullable) {

                ValidationFailureData failureData = new ValidationFailureData(
                    CommonValidationFailureReason.NOT_PRESENT,
                    fieldName
                );

                ValidationFailureResponse failureResponse = new ValidationFailureResponse(
                    failureData
                );

                throw new ValidationException(
                    failureResponse,
                    failureData
                );

            }

        } else {

            for (ValidationRule validationRule : validationRules) {

                validationRule.validate(fieldName, fieldValue);

            }

        }

    }

    public static class FieldValidatorBuilder {

        private PrioritySet<ValidationRule> validationRules = new PrioritySet<>();
        private boolean nullable = false;

        public FieldValidatorBuilder validationRules(ValidationRule...validationRules) {

            Collections.addAll(this.validationRules, validationRules);

            return this;

        }

        public FieldValidatorBuilder validationRules(Collection<ValidationRule> validationRules) {

            this.validationRules.addAll(validationRules);

            return this;

        }

        public FieldValidatorBuilder nullable() {

            this.nullable = true;

            return this;

        }

        public FieldValidatorBuilder notNullable() {

            this.nullable = false;

            return this;

        }

        public FieldValidator build() {

            return new FieldValidator(
                validationRules,
                nullable
            );

        }

    }

}
