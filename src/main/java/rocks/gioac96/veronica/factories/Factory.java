package rocks.gioac96.veronica.factories;

/**
 * Factory interface.
 *
 * @param <T> Type to instantiate
 */
public interface Factory<T> {

    void configure();

    T build() throws CreationException;

}
