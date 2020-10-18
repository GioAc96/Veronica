package rocks.gioac96.veronica.common.validation_rules;

import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Singleton;
import rocks.gioac96.veronica.validation.ValidationException;
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

        return fieldValue -> {

            try {

                Double.parseDouble(fieldValue);

            } catch (NumberFormatException e) {

                throw new ValidationException(e, failureReason);

            }


        };

    }

}
