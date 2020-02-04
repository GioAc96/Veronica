package org.gioac96.veronica.routing.pipeline.validation.rules;

import org.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;

/**
 * Validation rule that checks that a value represents a value email address.
 */
public class EmailRule extends RegexRule {

    public EmailRule() {

        super("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        super.validate(fieldName, fieldValue, CommonValidationFailureReason.INVALID_EMAIL);

    }

}
