package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.Getter;
import rocks.gioac96.veronica.http.Response;

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
