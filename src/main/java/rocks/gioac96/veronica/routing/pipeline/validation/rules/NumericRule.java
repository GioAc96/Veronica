package rocks.gioac96.veronica.routing.pipeline.validation.rules;

import rocks.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationException;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureData;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationFailureResponse;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationRule;

/**
 * Validation rule that checks that a value is numeric.
 */
@SuppressWarnings("unused")
public final class NumericRule implements ValidationRule {

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static NumericRule build() {

        return new NumericRule();

    }

    @SuppressWarnings("unused")
    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        try {

            Double.parseDouble(fieldValue);

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

    }

}
