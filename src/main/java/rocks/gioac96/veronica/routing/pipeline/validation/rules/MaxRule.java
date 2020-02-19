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
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationRule;


/**
 * Validation rule that checks that a value is below a specified maximum.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MaxRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    private Double maxValue;

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    private Boolean inclusive = true;

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        double value;

        try {

            value = Double.parseDouble(fieldValue);

        } catch (NumberFormatException e) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .failureReason(CommonValidationFailureReason.OUT_OF_RANGE)
                .fieldName(fieldName)
                .build();

            ValidationFailureResponse failureResponse = ValidationFailureResponse.builder()
                .validationFailureData(failureData)
                .build();

            throw new ValidationException(failureResponse, failureData);

        }

        if (
            (inclusive && value > maxValue)
                || (!inclusive && value >= maxValue)
        ) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .failureReason(CommonValidationFailureReason.OUT_OF_RANGE)
                .fieldName(fieldName)
                .build();

            ValidationFailureResponse failureResponse = ValidationFailureResponse.builder()
                .validationFailureData(failureData)
                .build();

            throw new ValidationException(failureResponse, failureData);

        }

    }

}
