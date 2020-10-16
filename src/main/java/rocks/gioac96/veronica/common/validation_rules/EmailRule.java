package rocks.gioac96.veronica.common.validation_rules;

import java.util.regex.Pattern;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.Singleton;

public class EmailRule
    extends PatternRule
    implements Singleton {

    @Override
    protected void configure() {


        pattern(Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$",
            Pattern.CASE_INSENSITIVE
        ));
        failureReason(CommonValidationFailureReasons.invalidEmail());

        super.configure();

    }

}
