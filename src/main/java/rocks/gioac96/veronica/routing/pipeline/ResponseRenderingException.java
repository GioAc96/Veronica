package rocks.gioac96.veronica.routing.pipeline;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * Pipeline exception thrown by the {@link ResponseRenderer}.
 */

public final class ResponseRenderingException extends PostRenderPipelineBreakException {

    @Getter
    @NonNull
    private final Response unrenderableResponse;

    @Builder
    public ResponseRenderingException(@NonNull Response fallbackResponse, @NonNull Response unenderableResponse) throws UnrenderedResponseException {

        super(fallbackResponse);

        this.unrenderableResponse = unenderableResponse;

    }

}
