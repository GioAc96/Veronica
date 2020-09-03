package rocks.gioac96.veronica.validation.rules;

import rocks.gioac96.veronica.util.ArraySet;
import rocks.gioac96.veronica.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationException;

/**
 * Validation rule that checks that a value represents a boolean.
 */
@SuppressWarnings("unused")
public final class BooleanRule extends InArrayRule {

    public BooleanRule() {

        super(ArraySet.of(
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

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static BooleanRule build() {

        return new BooleanRule();

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        super.validate(fieldName, fieldValue, CommonValidationFailureReason.NOT_BOOLEAN);

    }

}
