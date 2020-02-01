package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;

@RequiredArgsConstructor
public class InArrayRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private String[] allowedValues;

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        for (String allowedValue : allowedValues) {

            if (fieldValue.equals(allowedValue)) {

                return;

            }

        }

        ValidationFailureData failureData = new ValidationFailureData(
            ValidationFailureReason.NOT_IN_ARRAY,
            fieldName
        );

        ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
            failureData
        );

        throw new ValidationException(validationFailureResponse, failureData);

    }

}
