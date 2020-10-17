package rocks.gioac96.veronica.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static rocks.gioac96.veronica.common.CommonValidationRulesTest.assertValidationFails;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;

class PredicateRuleTest {

    @Test
    void testPredicateFails() {

        assertValidationFails(
            new PredicateRule()
                .predicate(value -> false)
                .provide(),
            "val",
            CommonValidationFailureReasons.generic()
        );

    }
    @Test
    void testPredicateSucceeds() throws ValidationException {

        new PredicateRule()
            .predicate(value -> true)
            .provide()
            .validate(
                "value"
            );

    }

    @Test
    void testInvalidPredicateRule() {

        assertThrows(
            CreationException.class,
            () -> new PredicateRule()
                .provide()
        );

    }

}