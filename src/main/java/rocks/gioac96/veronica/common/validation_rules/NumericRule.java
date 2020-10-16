package rocks.gioac96.veronica.common.validation_rules;

import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Singleton;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class NumericRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements Singleton {

    @Override
    protected void configure() {

        failureReason(CommonValidationFailureReasons.notNumeric());

        super.configure();

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
                    .provide();

                throw new ValidationException(e, failureData);

            }


        };

    }

}
