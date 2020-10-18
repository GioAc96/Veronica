package rocks.gioac96.veronica.common.validation_failure_reasons;

import rocks.gioac96.veronica.providers.Singleton;
import rocks.gioac96.veronica.validation.ValidationFailureReason;

public class IsNull
    extends ValidationFailureReason.ValidationFailureReasonBuilder
    implements Singleton {

    @Override
    protected void configure() {

        message("field can not be left empty");

        super.configure();

    }

}
