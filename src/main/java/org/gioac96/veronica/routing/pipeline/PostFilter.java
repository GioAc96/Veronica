package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} stage that filters a {@link Request} after a {@link Response} was generated.
 */
public interface PostFilter {

    void filter(@NonNull Request request, @NonNull Response response) throws PipelineBreakException;

}
