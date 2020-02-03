package org.gioac96.veronica.routing.pipeline.validation;

import lombok.Getter;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.pipeline.PipelineBreakException;

/**
 * Exception thrown by validation filters on request validation failure.
 */
public class ValidationException extends PipelineBreakException {

    @Getter
    private final ValidationFailureData validationFailureData;

    public ValidationException(Response response, ValidationFailureData validationFailureData) {

        super(response);
        this.validationFailureData = validationFailureData;

    }

}