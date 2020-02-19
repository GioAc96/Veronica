package rocks.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationException;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureReason;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationRule;

/**
 * {@link ValidationRule} that checks that a field's value matches a specified regex pattern.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RegexRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private String pattern;

    protected void validate(
        String fieldName,
        String fieldValue,
        ValidationFailureReason failureReason
    ) throws ValidationException {

        if (!fieldValue.matches(pattern)) {

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

        validate(fieldName, fieldValue, CommonValidationFailureReason.PATTERN_NOT_MATCHED);

    }

}
