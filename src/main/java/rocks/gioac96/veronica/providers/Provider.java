package rocks.gioac96.veronica.factories;

/**
 * Factory interface.
 *
 * @param <T> Type to instantiate
 */
public interface Factory<T> {

    T build() throws CreationException;


    default void configure() {

    }

}
