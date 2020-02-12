package org.gioac96.veronica.routing;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.routing.matching.CommonRequestMatchers;
import org.gioac96.veronica.routing.matching.RequestMatcher;
import org.gioac96.veronica.routing.pipeline.Pipeline;
import org.gioac96.veronica.routing.pipeline.RequestHandler;

/**
 * Application route.
 */
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Route<Q extends Request, S extends Response> {

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    private RequestMatcher<Q> requestMatcher = CommonRequestMatchers.alwaysMatch();

    @Getter
    @Setter
    @NonNull
    private RequestHandler<Q, S> requestHandler;

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    private Pipeline<Q, S> pipeline = Pipeline.<Q, S>builder().build();

    /**
     * Checks whether the route should handle the specified {@link Request}.
     *
     * @param request request to handle
     * @return true iff the route should handle the specified {@link Request}
     */
    public boolean shouldHandle(@NonNull Q request) {

        return requestMatcher.matches(request);

    }

    /**
     * Handles the specified {@link Request} via passing it through the route's {@link Pipeline}.
     *
     * @param request request to handle
     * @return the generated {@link Response}
     */
    public S handle(@NonNull Q request) {

        return pipeline.handle(request, requestHandler);

    }

}
