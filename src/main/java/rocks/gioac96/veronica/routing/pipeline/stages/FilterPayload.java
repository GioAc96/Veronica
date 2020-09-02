package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * Payload of pipeline filters.
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterPayload {

    @Getter
    private final Response response;

    private final boolean shouldContinue;

    /**
     * Instantiates a "continue pipeline" filter payload.
     *
     * @return the instantiated"continue pipeline" payload
     */
    public static  FilterPayload ok() {

        return new FilterPayload(null, true);

    }

    /**
     * Instantiates a "break pipeline" filter payload.
     *
     * @param response the response
     * @return the instantiated "break pipeline" payload
     */
    public static  FilterPayload fail(@NonNull Response response) {

        return new FilterPayload(response, false);

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
