package rocks.gioac96.veronica.providers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Basic builder class.
 * @param <T> type of the build object
 */
public abstract class Builder<T> implements Provider<T>, BuildsInstances {

    private static final class InstanceStore {

        private static final Map<Class<?>, Object> instancesMap = new HashMap<>();

        public static final <T> void store(Class<T> builderClass, Object instance) {

            instancesMap.put(builderClass, instance);

        }

        public static final <T> Object retrieve(Class<T> builderClass) {

            return instancesMap.get(builderClass);

        }

    }

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


        if (!isConfigured) {

            configure();

            if (! isValid()) {

                throw new CreationException();

            }

            isConfigured = true;

        }

        if (buildsMultipleInstances()) {

            return instantiate();

        } else {

            T storedInstance = (T)InstanceStore.retrieve(this.getClass());

            if (storedInstance == null) {

                T newInstance = instantiate();

                InstanceStore.store(this.getClass(), newInstance);

                return newInstance;

            } else {

                return storedInstance;

            }

        }

    }


}
