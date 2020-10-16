package rocks.gioac96.veronica.common.validation_rules;

import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.common.CommonValidationRules;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;

class EmailRuleTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "giorgio",
        "giorgio@",
        "giorgio@gmail",
        "giorgio@gmail.",
        "giorgio!@gmail.com",
        "giorgio?@gmail.com"
    })
    void testEmailFails(String value) {

        assertValidationFails(
            CommonValidationRules.email(),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.invalidEmail())
                .build()
        );

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "gioac96@gmail.com",
        "giorgio.acquati@gioac96.rocks",
        "giorgio@mail.com",
        "john_doe@email.com"
    })
    void testEmailOk(String value) throws ValidationException {

        CommonValidationRules.email().validate("test", value);

    }

}
