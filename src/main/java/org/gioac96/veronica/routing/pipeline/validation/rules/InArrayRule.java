package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.validation.DefaultValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;


/**
 * Validation rule that checks that a field's value is among the specified valid values.
 */
@RequiredArgsConstructor
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private String[] allowedValues;

    protected void validate(
        String fieldName,
        String fieldValue,
        ValidationFailureReason failureReason
    ) throws ValidationException {

        for (String allowedValue : allowedValues) {

            if (fieldValue.equals(allowedValue)) {

                return;

            }

        }

        ValidationFailureData failureData = new ValidationFailureData(
            failureReason,
            fieldName
        );

        ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
            failureData
        );

        throw new ValidationException(validationFailureResponse, failureData);

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        validate(fieldName, fieldValue, DefaultValidationFailureReason.NOT_IN_ARRAY);

    }

}
