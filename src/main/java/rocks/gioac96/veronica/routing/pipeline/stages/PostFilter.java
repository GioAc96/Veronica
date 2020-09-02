package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * {@link Pipeline} stage that filters a {@link Request} after a {@link Response} was generated.
 */
public interface PostFilter {

    FilterPayload filter(@NonNull Request request, @NonNull Response response);

}
