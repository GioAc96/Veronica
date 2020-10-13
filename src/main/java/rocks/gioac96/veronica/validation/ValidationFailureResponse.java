package rocks.gioac96.veronica.validation;

import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

/**
 * {@link Response} generated on validation failure.
 */
public class ValidationFailureResponse extends Response {

    protected static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;

    @Getter
    @NonNull
    protected final ValidationFailureData validationFailureData;

    protected ValidationFailureResponse(ValidationFailureResponseBuilder b) {

        super(b);
        this.validationFailureData = b.validationFailureData;
        this.httpStatus = b.httpStatus;

    }

    public static ValidationFailureResponseBuilder builder() {

        class ValidationFailureResponseBuilderImpl
            extends ValidationFailureResponseBuilder
            implements BuildsMultipleInstances {

        }

        return new ValidationFailureResponseBuilderImpl();

    }

    public abstract static class ValidationFailureResponseBuilder extends ResponseBuilder {

        private @NonNull ValidationFailureData validationFailureData;
        private @NonNull HttpStatus httpStatus = DEFAULT_HTTP_STATUS;

        public ValidationFailureResponseBuilder validationFailureData(@NonNull ValidationFailureData validationFailureData) {

            this.validationFailureData = validationFailureData;
            return this;

        }

        public ValidationFailureResponseBuilder httpStatus(@NonNull HttpStatus httpStatus) {

            this.httpStatus = httpStatus;
            return this;

        }


    }


}
