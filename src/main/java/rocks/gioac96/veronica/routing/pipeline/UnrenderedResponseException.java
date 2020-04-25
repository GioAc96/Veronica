package rocks.gioac96.veronica.routing.pipeline;

import lombok.Getter;
import rocks.gioac96.veronica.http.Response;

public class UnrenderedResponseException extends PipelineFailureException {

    @Getter
    private final Response unrenderedResponse;

    public UnrenderedResponseException(Response unrenderedResponse) {

        this.unrenderedResponse = unrenderedResponse;

    }

}
