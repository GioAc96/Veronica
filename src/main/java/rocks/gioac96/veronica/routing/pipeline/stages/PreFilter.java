package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * Pipeline stage that filters a {@link Request} before a {@link Response}
 * is generated.
 */
public interface PreFilter<Q extends Request, S extends Response> {

    FilterPayload<S> filter(@NonNull Q request);

}