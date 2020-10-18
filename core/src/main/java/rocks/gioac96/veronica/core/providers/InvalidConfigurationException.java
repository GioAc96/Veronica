package rocks.gioac96.veronica.core.providers;

public class InvalidConfigurationException extends CreationException {

    private final ConfigurableProvider<?> invalidConfigurableProvider;

    public InvalidConfigurationException(
        ConfigurableProvider<?> invalidConfigurableProvider
    ) {

        this.invalidConfigurableProvider = invalidConfigurableProvider;

    }

    public InvalidConfigurationException(
        Throwable cause,
        ConfigurableProvider<?> invalidConfigurableProvider
    ) {

        super(cause);
        this.invalidConfigurableProvider = invalidConfigurableProvider;

    }


}
