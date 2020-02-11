package org.gioac96.veronica.routing.pipeline;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.gioac96.veronica.http.Response;

/**
 * {@link Pipeline} error exception. It is thrown by any of the stages in a {@link Pipeline} that breaks the
 * execution of the pipeline itself.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PipelineBreakException extends Exception {

    @Getter
    @NonNull
    private final Response response;

}
