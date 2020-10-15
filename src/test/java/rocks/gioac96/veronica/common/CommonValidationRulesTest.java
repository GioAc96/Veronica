package rocks.gioac96.veronica.common;

import static org.junit.jupiter.api.Assertions.*;

import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class CommonValidationRulesTest {

    public static void assertValidationFails(
        ValidationRule rule,
        String fieldName,
        String fieldValue,
        ValidationFailureData expected
    ) {

        try {

            rule.validate(fieldName, fieldValue);

            fail();

        } catch (ValidationException e) {

            assertEquals(expected, e.getValidationFailureData());

        }

    }

}