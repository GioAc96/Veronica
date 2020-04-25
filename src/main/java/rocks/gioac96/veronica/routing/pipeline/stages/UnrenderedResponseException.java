package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rocks.gioac96.veronica.http.Response;

@AllArgsConstructor
public class UnrenderedResponseException extends PipelineFailureException {

    @Getter
    private final Response draftResponse;

}
