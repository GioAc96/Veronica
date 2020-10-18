package rocks.gioac96.veronica.validation.common.validation_rules;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.core.providers.CreationException;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.validation.common.CommonValidationRulesTest;

class PredicateRuleTest {

    @Test
    void testPredicateFails() {

        CommonValidationRulesTest.assertValidationFails(
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