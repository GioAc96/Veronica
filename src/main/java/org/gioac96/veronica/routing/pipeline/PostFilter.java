package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;

public interface PostFilter {

    void filter(@NonNull Request request, @NonNull Response response) throws PipelineBreakException;

}
