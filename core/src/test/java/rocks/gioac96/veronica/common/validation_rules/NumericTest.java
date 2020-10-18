package rocks.gioac96.veronica.common.validation_rules;

import static rocks.gioac96.veronica.common.CommonValidationRules.numeric;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.ValidationException;

class NumericTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "ciao",
        "",
        "1z",
        "34u5io",
        "1 0"
    })
    void testNumericFails(
        String value
    ) {

        assertValidationFails(
            numeric(),
            value,
            CommonValidationFailureReasons.notNumeric()
        );

    }

    @Test
    void testNumericOk() throws ValidationException {

        for (int i = 0; i < 100; i++) {

            numeric().validate(
                String.valueOf(new Random().nextDouble())
            );

        }

    }

}