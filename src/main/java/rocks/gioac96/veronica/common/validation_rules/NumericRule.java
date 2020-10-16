package rocks.gioac96.veronica.common.validation_rules;

import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class NumericRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsSingleInstance {

    @Override
    protected void configure() {

        super.configure();

        failureReason(CommonValidationFailureReasons.notNumeric());

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
