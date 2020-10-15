package rocks.gioac96.veronica.common.validation_rules;

import java.util.regex.Pattern;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;

public class EmailRule extends PatternRule {

    @Override
    protected void configure() {

        super.configure();

        pattern(Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$",
            Pattern.CASE_INSENSITIVE)
        );
        failureReason(CommonValidationFailureReasons.invalidEmail());

    }

    @Override
    public boolean buildsMultipleInstances() {

        return false;

    }

}
