package rocks.gioac96.veronica.common.validation_rules;

import java.util.Arrays;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;

public class BooleanRule extends InArray {

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

        super.configure();

        failureReason(CommonValidationFailureReasons.notBoolean());

        Arrays.stream(TRUE_VALUES).forEach(this::allowedValue);
        Arrays.stream(FALSE_VALUES).forEach(this::allowedValue);

    }

    @Override
    public boolean buildsMultipleInstances() {

        return false;

    }

}
