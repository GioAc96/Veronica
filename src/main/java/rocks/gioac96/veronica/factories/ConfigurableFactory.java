package rocks.gioac96.veronica.factories;

/**
 * Configurable factory interface.
 *
 * @param <T> Type to instantiate
 */
public interface ConfigurableFactory<T> extends Factory<T> {

    void configure();

}
