package rocks.gioac96.veronica.factories;

/**
 * Factory interface.
 *
 * @param <T> Type to instantiate
 */
public interface Factory<T> {

    void configure();

    T build() throws CreationException;

    /**
     * Gets the priority assigned to the instantiated object. Defaults to null.
     *
     * @return the priority assigned to the instantiated object.
     */
    default Integer priority() {

        return null;

    }

}
