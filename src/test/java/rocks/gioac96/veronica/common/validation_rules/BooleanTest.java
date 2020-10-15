package rocks.gioac96.veronica.common.validation_rules;

import static rocks.gioac96.veronica.common.CommonValidationRules.bool;
import static rocks.gioac96.veronica.common.CommonValidationRules.numeric;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;

public class BooleanTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "value is true",
        "yes sir",
        "whatever",
        "null",
        "12"
    })
    void testBooleanFails(
        String value
    ) {

        assertValidationFails(
            bool(),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.notBoolean())
                .build()
        );

    }

    private static Object[][] okArgs() {

        Object[][] args = new Object[BooleanRule.TRUE_VALUES.length + BooleanRule.FALSE_VALUES.length][1];

        int i = 0;

        for (; i < BooleanRule.TRUE_VALUES.length; i++) {

            args[i] = new String[1];
            args[i][0] = BooleanRule.TRUE_VALUES[i];

        }
        for (int j = 0; j < BooleanRule.FALSE_VALUES.length; j++, i++) {

            args[i] = new String[1];
            args[i][0] = BooleanRule.FALSE_VALUES[j];

        }

        return args;

    }

    @ParameterizedTest
    @MethodSource("okArgs")
    void testBooleanOk(String value) throws ValidationException {

        bool().validate("test", value);

    }

}
