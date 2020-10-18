package rocks.gioac96.veronica.validation.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.common.CommonValidationRules;
import rocks.gioac96.veronica.validation.common.CommonValidationRulesTest;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;

class MaxRuleTest {

    @ParameterizedTest
    @CsvSource({
        "1, 0",
        "0, -1",
        "0.1, 0",
        "-1, -2",
        "13491348.00001, 13491348",
        "-138718.9, -138718.91"
    })
    void testMaxFails(
        double value,
        double max
    ) {

        CommonValidationRulesTest.assertValidationFails(
            CommonValidationRules.max(max),
            String.valueOf(value),
            CommonValidationFailureReasons.tooBig(max)
        );

    }

    @ParameterizedTest
    @CsvSource({
        "0, 1",
        "1, 2",
        "1.000001, 1.000001",
        "12345, 12345",
        "123456.00001, 123456.00002"
    })
    void testMaxOk(
        double value,
        double max
    ) throws ValidationException {

        CommonValidationRules.max(max).validate( String.valueOf(value));

    }

    @Test
    void testInvalidMax() {

        assertThrows(CreationException.class, () -> {
            new MaxRule().provide();
        });


    }

}
