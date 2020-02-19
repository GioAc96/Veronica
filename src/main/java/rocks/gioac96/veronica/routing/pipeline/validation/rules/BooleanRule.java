package rocks.gioac96.veronica.routing.pipeline.validation.rules;

import rocks.gioac96.veronica.routing.pipeline.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.routing.pipeline.validation.ValidationException;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Validation rule that checks that a value represents a boolean.
 */
public final class BooleanRule extends InArrayRule {

    public BooleanRule() {

        super(ArraySet.of(String.class,
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
        ));

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static BooleanRule build() {

        return new BooleanRule();

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        super.validate(fieldName, fieldValue, CommonValidationFailureReason.NOT_BOOLEAN);

    }

}
