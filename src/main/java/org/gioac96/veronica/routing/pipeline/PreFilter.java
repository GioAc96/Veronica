package org.gioac96.veronica.routing.pipeline;

import lombok.NonNull;
import org.gioac96.veronica.http.Request;

public interface PreFilter {

    void filter(@NonNull Request request) throws PipelineBreakException;

}
