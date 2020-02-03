package org.gioac96.veronica.routing.pipeline.validation;

import lombok.NonNull;
import org.gioac96.veronica.http.HttpStatus;
import org.gioac96.veronica.http.Response;

/**
 * {@link Response} generated on validation failure.
 */
public class ValidationFailureResponse extends Response {

    private final ValidationFailureData validationFailureData;

    public ValidationFailureResponse(
        @NonNull HttpStatus httpStatus,
        ValidationFailureData validationFailureData
    ) {

        super(httpStatus);
        this.validationFailureData = validationFailureData;

    }

    public ValidationFailureResponse(ValidationFailureData validationFailureData) {

        super(HttpStatus.BAD_REQUEST);
        this.validationFailureData = validationFailureData;

    }

}
