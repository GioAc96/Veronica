package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} stage that filters a {@link Request} after a {@link Response} was generated.
 */
public interface PostFilter<Q extends Request, S extends Response> {

    void filter(@NonNull Q request, @NonNull S response) throws PipelineBreakException;

}
