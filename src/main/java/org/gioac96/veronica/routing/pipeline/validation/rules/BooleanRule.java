package org.gioac96.veronica.routing.pipeline.validation.rules;

import org.gioac96.veronica.routing.pipeline.validation.DefaultValidationFailureReason;
import org.gioac96.veronica.routing.pipeline.validation.ValidationException;

/**
 * Validation rule that checks that a value represents a boolean.
 */
public class BooleanRule extends InArrayRule {

    public BooleanRule() {

        super(new String[]{
            "true",
            "false",
            "on",
            "off",
            "yes",
            "no",
            "ok",
            "1",
            "0",
            ""
        });

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        super.validate(fieldName, fieldValue, DefaultValidationFailureReason.NOT_BOOLEAN);

    }
}
