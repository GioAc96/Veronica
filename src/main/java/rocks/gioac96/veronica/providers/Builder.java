package rocks.gioac96.veronica.providers;

import java.util.Arrays;
import java.util.Objects;

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

    protected static boolean isNotNull(Object... fields) {

        return Arrays.stream(fields).allMatch(Objects::nonNull);

    }

    protected boolean isValid() {

        return true;

    }

    @Override
    public final T provide() {

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

                if (! isValid()) {

                    throw new CreationException();

                }

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
