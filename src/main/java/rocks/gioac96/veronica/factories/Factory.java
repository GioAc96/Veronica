package rocks.gioac96.veronica.factories;

public interface Factory<T> {

    void configure();

    T build() throws CreationException;

    default Integer priority() {

        return null;

    }

}
