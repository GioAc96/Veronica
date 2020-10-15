package rocks.gioac96.veronica.common.validation_failure_reasons;

import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class IsNull extends ValidationFailureReason.ValidationFailureReasonBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        super.configure();

        message("field can not be left empty");

    }

}
