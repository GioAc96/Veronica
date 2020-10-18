package rocks.gioac96.veronica.validation.common.validation_rules;

import java.util.Arrays;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.core.providers.Singleton;

public class BooleanRule
    extends InArray
    implements Singleton {

    public static final String[] TRUE_VALUES = {
        "true",
        "on",
        "yes",
        "ok",
        "1",
        ""
    };
    public static final String[] FALSE_VALUES = {
        "false",
        "off",
        "no",
        "0",
    };

    @Override
    protected void configure() {

        failureReason(CommonValidationFailureReasons.notBoolean());

        Arrays.stream(TRUE_VALUES).forEach(this::allowedValue);
        Arrays.stream(FALSE_VALUES).forEach(this::allowedValue);

        super.configure();

    }

}
