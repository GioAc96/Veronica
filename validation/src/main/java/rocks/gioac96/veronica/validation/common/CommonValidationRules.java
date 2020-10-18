package rocks.gioac96.veronica.validation.common;

import static rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons.generic;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import rocks.gioac96.veronica.validation.ValidationFailureReason;
import rocks.gioac96.veronica.validation.ValidationRule;
import rocks.gioac96.veronica.validation.common.validation_rules.BetweenRule;
import rocks.gioac96.veronica.validation.common.validation_rules.BooleanRule;
import rocks.gioac96.veronica.validation.common.validation_rules.EmailRule;
import rocks.gioac96.veronica.validation.common.validation_rules.InArray;
import rocks.gioac96.veronica.validation.common.validation_rules.MaxRule;
import rocks.gioac96.veronica.validation.common.validation_rules.MinRule;
import rocks.gioac96.veronica.validation.common.validation_rules.NumericPredicateRule;
import rocks.gioac96.veronica.validation.common.validation_rules.NumericRule;
import rocks.gioac96.veronica.validation.common.validation_rules.PatternRule;
import rocks.gioac96.veronica.core.providers.Provider;

public class CommonValidationRules {

    private static final Provider<ValidationRule> bool = new BooleanRule();
    private static final Provider<ValidationRule> email = new EmailRule();
    private static final Provider<ValidationRule> numeric = new NumericRule();

    public static ValidationRule between(double min, double max) {

        return new BetweenRule()
            .minValue(min)
            .maxValue(max)
            .provide();

    }

    public static ValidationRule bool() {

        return bool.provide();

    }

    public static ValidationRule email() {

        return email.provide();

    }

    public static ValidationRule inArray(String... values) {

        return new InArray()
            .allowedValues(Arrays.asList(values))
            .provide();

    }

    public static ValidationRule max(double maxValue) {

        return new MaxRule()
            .maxValue(maxValue)
            .provide();

    }

    public static ValidationRule min(double minValue) {

        return new MinRule()
            .minValue(minValue)
            .provide();

    }

    public static ValidationRule numericPredicate(Predicate<Double> predicate) {

        return numericPredicate(
            predicate,
            generic()
        );

    }

    public static ValidationRule numericPredicate(
        Predicate<Double> predicate,
        ValidationFailureReason predicateNotAppliesFailureReason
    ) {

        return new NumericPredicateRule()
            .predicate(predicate)
            .predicateNotAppliesFailureReason(predicateNotAppliesFailureReason)
            .provide();

    }

    public static ValidationRule numeric() {

        return numeric.provide();

    }

    public static ValidationRule pattern(String pattern) {

        return new PatternRule()
            .patternString(pattern)
            .provide();

    }

    public static ValidationRule pattern(Pattern pattern) {

        return new PatternRule()
            .pattern(pattern)
            .provide();

    }

    public static ValidationRule pattern(String pattern, ValidationFailureReason failureReason) {

        return new PatternRule()
            .patternString(pattern)
            .failureReason(failureReason)
            .provide();

    }

    public static ValidationRule pattern(Pattern pattern, ValidationFailureReason failureReason) {

        return new PatternRule()
            .pattern(pattern)
            .failureReason(failureReason)
            .provide();

    }

}
