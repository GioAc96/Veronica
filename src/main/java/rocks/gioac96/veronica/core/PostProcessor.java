package rocks.gioac96.veronica.core;

import lombok.NonNull;

/**
 * {@link Pipeline} stage that processes a {@link Request} after the {@link Response} was already sent.
 */
public interface PostProcessor {

    void process(@NonNull Request request, @NonNull Response response);

    /**
     * Marker for PostProcessors that are executed asynchronously.
     */
    interface Asynchronous extends PostProcessor {

        int DEFAULT_PRIORITY = Integer.MAX_VALUE / 2;

        default Integer priority() {

            return DEFAULT_PRIORITY;

        }

    }

}
