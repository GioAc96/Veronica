package rocks.gioac96.veronica.common.validation_rules;

import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class EmailRule extends RegexRule {

    @Override
    protected void configure() {

        super.configure();

        pattern("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
        failureReason(ValidationFailureReason.builder()
            .message("value is not a valid email address")
            .build());

    }

    @Override
    public boolean buildsMultipleInstances() {

        return false;

    }

}
