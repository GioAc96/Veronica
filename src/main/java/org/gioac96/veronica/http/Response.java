package org.gioac96.veronica.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.gioac96.veronica.routing.pipeline.ResponseRenderer;

/**
 * Http response.
 */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Response {

    @Getter
    @Setter
    @NonNull
    protected HttpStatus httpStatus;

    @Getter
    @Builder.Default
    private boolean isRendered = false;

    @Getter
    @Builder.Default
    private String body = null;

    /**
     * Renders the response with the specified renderer.
     *
     * @param responseRenderer renderer used to render the response
     */
    public void render(ResponseRenderer responseRenderer) {

        if (isRendered) {

            throw new SecurityException("Response body was already set");

        }

        this.body = responseRenderer.render(this);
        isRendered = true;

    }

}
