package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;


/**
 * Validation rule that checks that a value is below a specified maximum.
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class MaxRule implements ValidationRule {

    @Getter
    @Setter
    private double maxValue;

    @Getter
    @Setter
    private boolean inclusive = true;

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        double value;

        try {

            value = Double.parseDouble(fieldValue);

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

        if (
            (inclusive && value > maxValue)
                || (!inclusive && value >= maxValue)
        ) {

            ValidationFailureData failureData = new ValidationFailureData(
                CommonValidationFailureReason.OUT_OF_RANGE,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }

    }

}
