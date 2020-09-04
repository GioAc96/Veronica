package rocks.gioac96.veronica.providers;

/**
 * Basic builder class.
 * @param <T> type of the build object
 */
public abstract class Builder<T> implements Provider<T> {

    private static Object instance = null;
    private static boolean isConfigured = false;

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

        if (instance == null) {

            if (!isConfigured) {

                configure();
                isConfigured = true;

            }

            if (this instanceof BuildsMultipleInstances) {

                return instantiate();

            } else {

                instance = instantiate();

            }

        }

        return (T) instance;

    }


}
