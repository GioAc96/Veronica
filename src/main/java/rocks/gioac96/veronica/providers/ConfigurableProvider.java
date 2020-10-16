package rocks.gioac96.veronica.providers;

import java.util.HashMap;
import java.util.HashSet;

public abstract class ConfigurableProvider<T> implements Provider<T> {

    private static final HashSet<ConfigurableProvider<?>> configuredProviders = new HashSet<>();
    private static final HashMap<Class<?>, Object> singletonInstances = new HashMap<>();

    protected void configure() {

    }


    private boolean isConfigured() {

        return configuredProviders.contains(this);

    }

    private void applyConfiguration() {

        configure();
        configuredProviders.add(this);

    }

    private boolean hasStoredInstance() {

        return singletonInstances.containsKey(this.getClass());

    }

    private T instantiateAndStoreInstance() {

        T instance = instantiate();

        singletonInstances.put(this.getClass(), instance);

        return instance;

    }

    @Override
    @SuppressWarnings("unchecked")
    public T provide() throws CreationException {

        try {

            if (this instanceof Singleton) {

                if (hasStoredInstance()) {

                    return (T) singletonInstances.get(this.getClass());

                } else {

                    configure();
                    checkValidity();

                    return instantiateAndStoreInstance();

                }

            } else {

                if (!isConfigured()) {

                    applyConfiguration();

                    checkValidity();

                }

                return instantiate();

            }

        } catch (Exception e) {

            throw new CreationException(e);

        }

    }

    private void checkValidity() throws InvalidConfigurationException {

        if (!isValid()) {

            throw new InvalidConfigurationException(this);

        }

    }

    protected boolean isValid() {

        return true;

    }

    protected abstract T instantiate();

}
