package rocks.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} stage that filters a {@link Request} after a {@link Response} was generated.
 */
public interface PostFilter<Q extends Request, S extends Response> {

    @SuppressWarnings("RedundantThrows")
    void filter(@NonNull Q request, @NonNull S response) throws PipelineBreakException;

}
