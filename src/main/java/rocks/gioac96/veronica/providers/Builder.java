package rocks.gioac96.veronica.providers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic builder class.
 *
 * @param <T> type of the build object
 */
public abstract class Builder<T> implements Provider<T>, BuildsInstances {

    private static final Set<Object> configuredBuilders = new HashSet<>();
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    private boolean isConfigured() {

        return configuredBuilders.contains(this);

    }

    private void configured() {

        configuredBuilders.add(this);

    }

    protected abstract T instantiate();

    protected void configure() {

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
     *
     * @return the built object
     */
    public final T build() {

        try {

            if (!isConfigured()) {

                configure();

                if (!isValid()) {

                    throw new CreationException();

                }

                configured();

            }

            if (buildsMultipleInstances()) {

                return instantiate();

            } else {

                T storedInstance = (T) instances.get(this.getClass());

                if (storedInstance == null) {

                    T newInstance = instantiate();

                    instances.put(this.getClass(), newInstance);

                    return newInstance;

                } else {

                    return storedInstance;

                }

            }

        } catch (Exception e) {

            throw new CreationException(e);

        }

    }

}
