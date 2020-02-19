package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;

/**
 * Pipeline stage that filters a {@link Request} before a {@link org.gioac96.veronica.http.Response}
 * is generated.
 */
public interface PreFilter<Q extends Request> {

    void filter(@NonNull Q request) throws PipelineBreakException;

}
