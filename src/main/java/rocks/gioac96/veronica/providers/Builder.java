package rocks.gioac96.veronica.providers;

/**
 * Basic builder class.
 * @param <T> type of the build object
 */
public abstract class Builder<T> implements Provider<T> {

    private T instance = null;

    protected abstract T instantiate();

    protected void configure() {

    }

    @Override
    public final T provide() throws CreationException {

        return build();

    }

    /**
     * Builds the object.
     * @return the built object
     */
    public final T build() {

        if (this instanceof BuildsMultipleInstances) {

            return instantiate();

        } else {

            if (instance == null) {

                instance = instantiate();

            }

            return instance;

        }

    }


}
