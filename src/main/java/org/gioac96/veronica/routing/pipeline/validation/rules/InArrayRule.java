package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedValues;

    protected void validate(
        String fieldName,
        String fieldValue,
        ValidationFailureReason failureReason
    ) throws ValidationException {

        if (allowedValues.none(allowedValue -> allowedValue.equals(fieldValue))) {

            ValidationFailureData failureData = new ValidationFailureData(
                failureReason,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        validate(fieldName, fieldValue, CommonValidationFailureReason.NOT_IN_ARRAY);

    }

}
