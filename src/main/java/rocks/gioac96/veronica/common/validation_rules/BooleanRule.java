package rocks.gioac96.veronica.common.validation_rules;

import java.util.Arrays;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class BooleanRule extends InArray {

    @Override
    protected void configure() {

        super.configure();

        failureReason(ValidationFailureReason.builder()
            .message("value must be either true or false")
            .build()
        );

        Arrays.stream(new String[]{
            "false",
            "true",
            "on",
            "off",
            "yes",
            "no",
            "ok",
            "1",
            "0",
            ""
        }).forEach(this::allowedValue);

    }

    @Override
    public boolean buildsMultipleInstances() {

        return false;

    }

}
