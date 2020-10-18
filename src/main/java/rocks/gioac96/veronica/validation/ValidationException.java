package rocks.gioac96.veronica.validation;

import lombok.Getter;

/**
 * Exception thrown by validation filters on request validation failure.
 */
@Getter
public class ValidationException extends Exception {

    private final ValidationFailureReason validationFailureReason;

    public ValidationException(Throwable cause, ValidationFailureReason validationFailureReason) {

        super(cause);

        this.validationFailureReason = validationFailureReason;

    }

    public ValidationException(ValidationFailureReason validationFailureReason) {

        super();

        this.validationFailureReason = validationFailureReason;

    }

}
