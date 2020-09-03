package rocks.gioac96.veronica.core;

import lombok.NonNull;

/**
 * {@link Pipeline} stage that filters a {@link Request} after a {@link Response} was generated.
 */
public interface PostFilter {

    void filter(@NonNull Request request, @NonNull Response response);

}
