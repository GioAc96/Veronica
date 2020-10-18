package rocks.gioac96.veronica.core.validation.validation_rules;

import rocks.gioac96.veronica.core.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Singleton;
import validation.ValidationException;
import validation.ValidationRule;

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
