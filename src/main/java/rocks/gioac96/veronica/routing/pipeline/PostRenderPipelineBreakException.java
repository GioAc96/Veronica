package rocks.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

public abstract class PostRenderPipelineBreakException extends PipelineBreakException {

    public PostRenderPipelineBreakException(@NonNull Response fallbackResponse) throws UnrenderedResponseException {

        super(fallbackResponse);

        if (! fallbackResponse.isRendered()) {

            throw new UnrenderedResponseException(fallbackResponse);

        }

    }

}
