package rocks.gioac96.veronica.common;

import rocks.gioac96.veronica.common.validation_failure_reasons.GenericFailureReason;
import rocks.gioac96.veronica.common.validation_failure_reasons.InvalidEmail;
import rocks.gioac96.veronica.common.validation_failure_reasons.IsNull;
import rocks.gioac96.veronica.common.validation_failure_reasons.NotBoolean;
import rocks.gioac96.veronica.common.validation_failure_reasons.NotInArray;
import rocks.gioac96.veronica.common.validation_failure_reasons.NotInRange;
import rocks.gioac96.veronica.common.validation_failure_reasons.NotNumeric;
import rocks.gioac96.veronica.common.validation_failure_reasons.PatternNotMatches;
import rocks.gioac96.veronica.common.validation_failure_reasons.ValueTooBig;
import rocks.gioac96.veronica.common.validation_failure_reasons.ValueTooSmall;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class CommonValidationFailureReasons {

    private static final Provider<ValidationFailureReason> isNull = new IsNull();
    private static final Provider<ValidationFailureReason> notNumeric = new NotNumeric();
    private static final Provider<ValidationFailureReason> notBoolean = new NotBoolean();
    private static final Provider<ValidationFailureReason> invalidEmail = new InvalidEmail();
    private static final Provider<ValidationFailureReason> patternNotMatches = new PatternNotMatches();
    private static final Provider<ValidationFailureReason> generic = new GenericFailureReason();
    private static final Provider<ValidationFailureReason> notInArray = new NotInArray();

    public static ValidationFailureReason isNull() {

        return isNull.provide();

    }

    public static ValidationFailureReason notNumeric() {

        return notNumeric.provide();

    }

    public static ValidationFailureReason notInRange(Double minValue, Double maxValue) {

        return new NotInRange()
            .minValue(minValue)
            .maxValue(maxValue)
            .provide();

    }

    public static ValidationFailureReason tooBig(Double maxValue) {

        return new ValueTooBig()
            .maxValue(maxValue)
            .provide();

    }

    public static ValidationFailureReason tooSmall(Double minValue) {

        return new ValueTooSmall()
            .minValue(minValue)
            .provide();

    }

    public static ValidationFailureReason notBoolean() {

        return notBoolean.provide();

    }

    public static ValidationFailureReason invalidEmail() {

        return invalidEmail.provide();

    }

    public static ValidationFailureReason patternNotMatches() {

        return patternNotMatches.provide();

    }

    public static ValidationFailureReason generic() {

        return generic.provide();

    }

    public static ValidationFailureReason notInArray() {

        return notInArray.provide();

    }

}
