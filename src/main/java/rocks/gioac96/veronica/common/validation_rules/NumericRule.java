package rocks.gioac96.veronica.common.validation_rules;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public class NumericRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsSingleInstance {

    public static final ValidationFailureReason FAILURE_REASON = ValidationFailureReason.builder()
        .message("value is not numeric")
        .build();

    @Override
    protected void configure() {

        super.configure();

        failureReason(FAILURE_REASON);

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            try {

                Double.parseDouble(fieldValue);

            } catch (NumberFormatException e) {

                ValidationFailureData failureData = ValidationFailureData.builder()
                    .failureReason(failureReason)
                    .fieldName(fieldName)
                    .build();

                throw new ValidationException(e, failureData);

            }


        };

    }

}
