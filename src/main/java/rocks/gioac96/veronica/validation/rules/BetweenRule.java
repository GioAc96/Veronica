package rocks.gioac96.veronica.validation.rules;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.validation.ValidationRule;

/**
 * Validation rule that checks that a value is between a maximum and a minimum.
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class BetweenRule implements ValidationRule {

    @Getter
    @Setter
    @NonNull
    protected Double minVal;

    @Getter
    @Setter
    @NonNull
    protected Double maxVal;

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    protected Boolean inclusive = false;

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

            ValidationFailureResponse failureResponse = ValidationFailureResponse.builder()
                .validationFailureData(failureData)
                .build();

            throw new ValidationException(failureResponse, failureData);

        }


        if (
            (inclusive && (value < minVal || value > maxVal))
                || (!inclusive && (value <= minVal || value > maxVal))
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
