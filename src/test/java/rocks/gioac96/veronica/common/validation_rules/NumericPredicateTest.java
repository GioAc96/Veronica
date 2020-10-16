package rocks.gioac96.veronica.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.common.CommonValidationRules;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class NumericPredicateTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "hi",
        "giorgio",
        "veronica",
        "one",
        "1a",
        "1 0"
    })
    void testNumericNotNumericValueFails(
        String value
    ) {

        assertValidationFails(
            CommonValidationRules.numericPredicate(val -> true),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(CommonValidationFailureReasons.notNumeric())
                .build()
        );

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1",
        "3",
        "5",
        "-1",
        "-19.0"
    })
    void testNumericPredicateFails(
        String value
    ) {

        ValidationFailureReason failureReason = ValidationFailureReason.builder()
            .message("number is not even")
            .build();

        assertValidationFails(
            CommonValidationRules.numericPredicate(val -> val % 2 == 0d, failureReason),
            "test",
            value,
            ValidationFailureData.builder()
                .fieldName("test")
                .failureReason(failureReason)
                .build()
        );

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2",
        "4",
        "-18",
        "124618624.000"
    })
    void testNumericPredicateSucceeds(
        String value
    ) throws ValidationException {

        ValidationFailureReason failureReason = ValidationFailureReason.builder()
            .message("number is not even")
            .build();

        CommonValidationRules.numericPredicate(val -> val % 2 == 0d, failureReason).validate(
            "test",
            value
        );

    }

    @Test
    void testNumericPredicateInvalid() {

        assertThrows(
            CreationException.class,
            () -> new NumericPredicateRule().build()
        );

    }

}
