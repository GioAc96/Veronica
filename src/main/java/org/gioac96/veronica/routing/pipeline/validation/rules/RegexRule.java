package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import org.gioac96.veronica.routing.pipeline.validation.ValidationRule;

@RequiredArgsConstructor
public class RegexRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private String pattern;

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        if (! fieldValue.matches(pattern)) {

            ValidationFailureData failureData = new ValidationFailureData(
                ValidationFailureReason.PATTERN_NOT_MATCHED,
                fieldName
            );

            ValidationFailureResponse validationFailureResponse = new ValidationFailureResponse(
                failureData
            );

            throw new ValidationException(validationFailureResponse, failureData);

        }

    }

}
