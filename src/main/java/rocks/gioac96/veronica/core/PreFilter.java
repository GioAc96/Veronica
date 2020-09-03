package rocks.gioac96.veronica.core;

import lombok.NonNull;

/**
 * Pipeline stage that filters a {@link Request} before a {@link Response}
 * is generated.
 */
public interface PreFilter {

    void filter(@NonNull Request request);

}
