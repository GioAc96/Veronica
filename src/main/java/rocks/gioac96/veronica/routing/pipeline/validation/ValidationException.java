package rocks.gioac96.veronica.routing.pipeline.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.PipelineBreakException;

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
