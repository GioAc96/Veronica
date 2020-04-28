package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rocks.gioac96.veronica.http.Response;

/**
 * Pipeline failure exception thrown when the pipeline fails to generate a rendered response. Includes an unrendered
 * response draft.
 */
@AllArgsConstructor
public class UnrenderedResponseException extends PipelineFailureException {

    @Getter
    private final Response draftResponse;

}
