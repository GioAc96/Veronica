package rocks.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * Pipeline stage that filters a {@link Request} before a {@link Response}
 * is generated.
 */
public interface PreFilter<Q extends Request> {

    void filter(@NonNull Q request) throws PipelineBreakException;

}
