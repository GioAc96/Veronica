package rocks.gioac96.veronica.routing.pipeline;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} error exception. It is thrown by any of the stages in a {@link Pipeline} that breaks the
 * execution of the pipeline itself.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PipelineBreakException extends Exception {

    @NonNull
    private final Response fallbackResponse;

    public <S extends Response> S getFallbackResponse() {

        return (S) fallbackResponse;

    }
}
