package org.gioac96.veronica.routing.pipeline.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.pipeline.PipelineBreakException;

/**
 * Exception thrown by validation filters on request validation failure.
 */
public class ValidationException extends PipelineBreakException {

    @Getter
    @NonNull
    private final ValidationFailureData validationFailureData;

    @Builder
    public ValidationException(
        @NonNull Response response,
        @NonNull ValidationFailureData validationFailureData
    ) {

        super(response);
        this.validationFailureData = validationFailureData;

    }

}
