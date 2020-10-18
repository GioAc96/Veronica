package rocks.gioac96.veronica.validation.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;

public class CommonValidationRulesTest {

    public static void assertValidationFails(
        ValidationRule rule,
        String fieldValue,
        ValidationFailureReason expectedFailure
    ) {

        try {

            rule.validate(fieldValue);

            fail();

        } catch (ValidationException e) {

            assertEquals(expectedFailure, e.getValidationFailureReason());

        }

    }

}