package rocks.gioac96.veronica.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.common.CommonValidationRules;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;

public class MinRuleTest {

    @ParameterizedTest
    @CsvSource({
        "0, -1",
        "10, 9.9999",
        "1000, -1000"
    })
    void testMinFails(
        double min,
        double value
    ) {

        assertValidationFails(
            CommonValidationRules.min(min),
            "test",
            String.valueOf(value),
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.tooSmall(min))
                .build()
        );

    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "0, 1",
        "-100, -100",
        "-134, -133.9999999",
        "1457.0139, 1457.014"
    })
    void testMinOk(
        double min,
        double value
    ) throws ValidationException {

        CommonValidationRules.min(min).validate("test", String.valueOf(value));

    }

    @Test
    void testInvalidMin() {

        assertThrows(CreationException.class, () -> {
            new MinRule().build();
        });


    }
    
}
