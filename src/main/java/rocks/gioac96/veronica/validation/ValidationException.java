package rocks.gioac96.veronica.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.PipelineBreakException;
import rocks.gioac96.veronica.core.Response;

/**
 * Exception thrown by validation filters on request validation failure.
 */
@Getter
public class ValidationException extends Exception {

    private final ValidationFailureData validationFailureData;

    public ValidationException(Throwable cause, ValidationFailureData validationFailureData) {

        super(cause);

        this.validationFailureData = validationFailureData;

    }

    public ValidationException(ValidationFailureData validationFailureData) {

        super();

        this.validationFailureData = validationFailureData;

    }

}
