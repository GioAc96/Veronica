package rocks.gioac96.veronica;

import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;

/**
 * Basic application factory, with default Request and Response types.
 */
public abstract class BasicApplicationFactory extends ApplicationFactory {

    @Override
    public Application build() throws CreationException {

        exchangeParser(new ExchangeParser() {});

        exceptionHandler(new ExceptionHandler() {});

        return super.build();

    }

}
