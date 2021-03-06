package rocks.gioac96.veronica.providers;

/**
 * Factory interface.
 *
 * @param <T> Type to instantiate
 */
public interface Provider<T> {

    T provide() throws CreationException;

}
