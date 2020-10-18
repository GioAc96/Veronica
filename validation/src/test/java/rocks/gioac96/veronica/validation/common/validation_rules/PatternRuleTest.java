package rocks.gioac96.veronica.validation.common.validation_rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.common.CommonValidationRules;
import rocks.gioac96.veronica.validation.common.CommonValidationRulesTest;

class PatternRuleTest {

    @ParameterizedTest
    @CsvSource({
        "1, [0-9]{2}, message1",
        "a, [0-9]{2}, message2"
    })
    void testPatternFailsWithMessage(
        String value,
        String pattern,
        String message
    ) {

        CommonValidationRulesTest.assertValidationFails(
            CommonValidationRules.pattern(pattern, ValidationFailureReason.builder().message(message).provide()),
            value,
            ValidationFailureReason.builder().message(message).provide()
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

        CommonValidationRulesTest.assertValidationFails(
            CommonValidationRules.pattern(pattern),
            value,
            CommonValidationFailureReasons.patternNotMatches()
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

        CommonValidationRules.pattern(pattern).validate(value);

    }

}