package org.gioac96.veronica.routing.pipeline.validation;

import lombok.Getter;
import lombok.Setter;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Field validator.
 */
public class FieldValidator {

    @Getter
    private PrioritySet<ValidationRule> validationRules;

    @Getter
    @Setter
    private boolean nullable;

    public FieldValidator() {

        validationRules = new PrioritySet<>();
        nullable = false;

    }

    public FieldValidator(boolean nullable) {

        this.nullable = nullable;

    }

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

}
