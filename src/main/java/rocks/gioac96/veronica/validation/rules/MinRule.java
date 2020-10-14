package rocks.gioac96.veronica.validation.rules;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;


/**
 * Validation rule that checks that a value is above a specified minimum.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class MinRule implements ValidationRule {

    @Getter
    @Setter
    private Double minValue;

    @Getter
    @Setter
    @Builder.Default
    private Boolean inclusive = true;


    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        double value;

        try {

            value = Double.parseDouble(fieldValue);

        } catch (NumberFormatException e) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .failureReason(CommonValidationFailureReason.NOT_NUMERIC)
                .fieldName(fieldName)
                .build();

            Response failureResponse = CommonResponses.validationFailure(failureData);

            throw new ValidationException(failureResponse, failureData);

        }

        if (
            (inclusive && value < minValue) || (!inclusive && value <= minValue)
        ) {

            ValidationFailureData failureData = ValidationFailureData.builder()
                .failureReason(CommonValidationFailureReason.OUT_OF_RANGE)
                .fieldName(fieldName)
                .build();

            Response failureResponse = CommonResponses.validationFailure(failureData);

            throw new ValidationException(failureResponse, failureData);

        }

    }

}
