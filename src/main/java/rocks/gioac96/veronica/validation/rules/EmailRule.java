package rocks.gioac96.veronica.validation.rules;

import rocks.gioac96.veronica.validation.CommonValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationException;

/**
 * Validation rule that checks that a value represents a value email address.
 */
@SuppressWarnings("unused")
public final class EmailRule extends RegexRule {

    @SuppressWarnings("unused")
    public EmailRule() {

        super("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static EmailRule build() {

        return new EmailRule();

    }

    @Override
    public void validate(String fieldName, String fieldValue) throws ValidationException {

        super.validate(fieldName, fieldValue, CommonValidationFailureReason.INVALID_EMAIL);

    }

}
