package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.NonNull;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.Pipeline;

/**
 * {@link Pipeline} stage that processes a {@link Request} after the {@link Response} was already sent.
 */
public interface PostProcessor {

    void process(@NonNull Request request, @NonNull Response response);

    /**
     * Marker for PostProcessors that are executed asynchronously.
     */
    interface Asynchronous extends PostProcessor {}

}
