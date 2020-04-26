package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

/**
 * Payload of pipeline filters.
 *
 * @param <S> Type of Response
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterPayload<S extends Response> {

    @Getter
    private final S response;

    private final boolean shouldContinue;

    /**
     * Instantiates a "continue pipeline" filter payload.
     *
     * @param <S> Type of the response
     * @return the instantiated"continue pipeline" payload
     */
    public static <S extends Response> FilterPayload<S> ok() {

        return new FilterPayload<>(null, true);

    }

    /**
     * Instantiates a "break pipeline" filter payload.
     *
     * @param response the response
     * @param <S>      type of the response
     * @return the instantiated "break pipeline" payload
     */
    public static <S extends Response> FilterPayload<S> fail(@NonNull S response) {

        return new FilterPayload<>(response, false);

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
