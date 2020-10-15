package rocks.gioac96.veronica.common.validation_rules;

import static org.junit.jupiter.api.Assertions.*;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.common.CommonValidationRules;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;

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

        assertValidationFails(
            CommonValidationRules.max(max),
            "test",
            String.valueOf(value),
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.tooBig(max))
                .build()
        );

    }

    @Test
    void testInvalidMax() {

        assertThrows(CreationException.class, () -> {
            new MaxRule().build();
        });


    }

}
