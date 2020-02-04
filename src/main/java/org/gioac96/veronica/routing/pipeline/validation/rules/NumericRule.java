package org.gioac96.veronica.routing.pipeline.validation.rules;

import org.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;

/**
 * Validation rule that checks that a value is numeric.
 */
public class NumericRule implements ValidationRule {

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        try {

            Double.parseDouble(fieldValue);

        } catch (NumberFormatException e) {

            ValidationFailureData failureData = new ValidationFailureData(
                CommonValidationFailureReason.NOT_NUMERIC,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }

    }

}
