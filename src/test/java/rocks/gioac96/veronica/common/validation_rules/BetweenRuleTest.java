package rocks.gioac96.veronica.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static rocks.gioac96.veronica.common.CommonValidationRules.between;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;

class BetweenRuleTest {

    @ParameterizedTest
    @CsvSource({
        "0, 2, 1",
        "1, -1, 2",
        "0, -0.1, 1",
        "0, 2.1, 2",
        "0, 0.5, 0.25",
        "-1, -1.5, 0.25",
        "1123675.12893, 1123675.12895, 1123675.12894",
        "1123675.12893, 1123675.12892, 1123675.12894"
    })
    void testBetweenFails(
        double min,
        double value,
        double max
    ) {

        assertValidationFails(
            between(min, max),
            "test",
            String.valueOf(value),
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.notInRange(min, max))
                .provide()
        );

    }

    @ParameterizedTest
    @CsvSource({
        "0, 1, 2",
        "5, 10, 1000",
        "-100, -1, 4",
        "-0.00001, 0, 0.00001",
        "8741384, 8741384.1, 8741384.2"
    })
    void testBetweenOk(
        double min,
        double value,
        double max
    ) throws ValidationException {

        between(min, max).validate("test", String.valueOf(value));

    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "-1234, -1234",
        "1, 0",
        "100, -100",
        "314892041.1331, 314892041.1331",
        "314892041.1331, 314892041.1330"
    })
    void testInvalidBetween(
        double min,
        double max
    ) {

        assertThrows(CreationException.class, () -> between(min, max));

    }

}