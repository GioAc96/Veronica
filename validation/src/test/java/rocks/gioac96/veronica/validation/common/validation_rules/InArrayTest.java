package rocks.gioac96.veronica.validation.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.common.CommonValidationRulesTest;

class InArrayTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "on",
        "wo",
        "tree",
        "for",
        "five",
        "six",
        "seven"
    })
    void testInArrayFails(String value) {

        CommonValidationRulesTest.assertValidationFails(
            new InArray()
                .allowedValue("one")
                .allowedValue("two")
                .allowedValue("three")
                .allowedValue("four")
                .provide(),
            "five",
            CommonValidationFailureReasons.notInArray()
        );

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "five",
        "six",
        "seven"
    })
    void testInArraySucceeds(String value) throws ValidationException {

        new InArray()
            .allowedValue("five")
            .allowedValue("six")
            .allowedValue("seven")
            .provide()
            .validate(value);

    }

    @Test
    void testInArrayInvalid() {

        assertThrows(CreationException.class, () -> new InArray().provide());

    }

}