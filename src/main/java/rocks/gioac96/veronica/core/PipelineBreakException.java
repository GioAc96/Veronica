package rocks.gioac96.veronica.core;

import lombok.Getter;

/**
 * Exception thrown when the pipeline fails to generate a response.
 */
public class PipelineBreakException extends RuntimeException {

    @Getter
    private final Response response;

    public PipelineBreakException(Response response) {

        super();
        this.response = response;

    }

    public PipelineBreakException(Throwable cause, Response response) {

        super(cause);
        this.response = response;

    }

}
