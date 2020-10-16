package rocks.gioac96.veronica.providers;

public class InvalidConfigurationException extends Exception {

    private final ConfigurableProvider<?> invalidConfigurableProvider;

    public InvalidConfigurationException(
        ConfigurableProvider<?> invalidConfigurableProvider
    ) {


        this.invalidConfigurableProvider = invalidConfigurableProvider;

    }


}
