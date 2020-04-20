package rocks.gioac96.veronica.routing.pipeline.validation;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rocks.gioac96.veronica.http.HttpStatus;
import rocks.gioac96.veronica.http.Response;

/**
 * {@link Response} generated on validation failure.
 */
@SuperBuilder
public class ValidationFailureResponse extends Response {

    protected static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    @Getter
    @NonNull
    private final ValidationFailureData validationFailureData;

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    private HttpStatus httpStatus = DEFAULT_HTTP_STATUS;

}