package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.Getter;
import org.gioac96.veronica.routing.pipeline.validation.DefaultValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;

/**
 * Validation rule that checks that a value is between a maximum and a minimum.
 */
public class BetweenRule implements ValidationRule {

    @Getter
    protected double minVal;

    @Getter
    protected double maxVal;

    @Getter
    protected boolean inclusive;

    public BetweenRule(double minVal, double maxVal) {

        this.minVal = minVal;
        this.maxVal = maxVal;

        this.inclusive = true;

    }

    public BetweenRule(double minVal, double maxVal, boolean inclusive) {

        this.minVal = minVal;
        this.maxVal = maxVal;
        this.inclusive = inclusive;

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        double value;

        try {

            value = Double.parseDouble(fieldValue);

        } catch (NumberFormatException e) {

            ValidationFailureData failureData = new ValidationFailureData(
                DefaultValidationFailureReason.NOT_NUMERIC,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }


        if (
            (inclusive && (value < minVal || value > maxVal))
                || (!inclusive && (value <= minVal || value > maxVal))
        ) {

            ValidationFailureData failureData = new ValidationFailureData(
                DefaultValidationFailureReason.OUT_OF_RANGE,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }

    }

}
