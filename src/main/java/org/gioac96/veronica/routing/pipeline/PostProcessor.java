package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} stage that processes a {@link Request} after the {@link Response} was already sent.
 */
public interface PostProcessor {

    void process(@NonNull Request request, @NonNull Response response);

}
