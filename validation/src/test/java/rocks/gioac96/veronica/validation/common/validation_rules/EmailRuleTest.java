package rocks.gioac96.veronica.validation.common.validation_rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.common.CommonValidationRules;
import rocks.gioac96.veronica.validation.common.CommonValidationRulesTest;
import rocks.gioac96.veronica.validation.ValidationException;

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

        CommonValidationRulesTest.assertValidationFails(
            CommonValidationRules.email(),
            value,
            CommonValidationFailureReasons.invalidEmail()
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

        CommonValidationRules.email().validate(value);

    }

}
