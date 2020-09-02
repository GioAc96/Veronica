package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * RequestHandler payload.
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHandlerPayload {

    @Getter
    @NonNull
    private final Response response;

    private final boolean shouldContinue;

    /**
     * Instantiates a "continue pipeline" request handler payload.
     *
     * @param response the response
     * @return the instantiated "continue pipeline" payload
     */
    public static  RequestHandlerPayload ok(Response response) {

        return new RequestHandlerPayload(response, true);

    }

    /**
     * Instantiates a "break pipeline" request handler payload.
     *
     * @param response the response
     * @return the instantiated "break pipeline" payload
     */
    public static  RequestHandlerPayload fail(Response response) {

        return new RequestHandlerPayload(response, false);

    }

    /**
     * Gets the "continue pipeline" flag value.
     *
     * @return true iff the "continue pipeline" flag value is true
     */
    public boolean shouldContinue() {

        return shouldContinue;

    }

}
