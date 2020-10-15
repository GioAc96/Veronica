package rocks.gioac96.veronica.common.validation_rules;

import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.common.CommonValidationRules;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

class PatternRuleTest {

    @ParameterizedTest
    @CsvSource({
        "1, [0-9]{2}, message1",
        "a, [0-9]{2}, message1"
    })
    void testPatternFailsWithMessage(
        String value,
        String pattern,
        String message
    ) {

        assertValidationFails(
            CommonValidationRules.pattern(pattern, message),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(ValidationFailureReason.builder()
                    .message(message)
                    .build())
                .build()
        );

    }

    @ParameterizedTest
    @CsvSource({
        "1, [0-9]{2}",
        "a, [0-9]{2}"
    })
    void testPatternFailsWithMessage(
        String value,
        String pattern
    ) {

        assertValidationFails(
            CommonValidationRules.pattern(pattern),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.patternNotMatches())
                .build()
        );

    }

    @ParameterizedTest
    @CsvSource({
        "1, [0-9]+",
        "a, [a-z]*"
    })
    void testPatternOk(
        String value,
        String pattern
    ) throws ValidationException {

        CommonValidationRules.pattern(pattern).validate("test", value);

    }

}