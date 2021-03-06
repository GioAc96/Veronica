package rocks.gioac96.veronica.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Response;

/**
 * Exception thrown by validation filters on request validation failure.
 */
public class ValidationException extends Exception {

    @Getter
    @NonNull
    private final ValidationFailureData validationFailureData;

    @Builder
    public ValidationException(
        @NonNull Response response,
        @NonNull ValidationFailureData validationFailureData
    ) {

        super();
        this.validationFailureData = validationFailureData;

    }

}
