package rocks.gioac96.veronica;

import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.http.BasicExchangeParser;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * Basic application factory, with default Request and Response types.
 */
public abstract class BasicApplicationFactory extends ApplicationFactory<Request, Response> {

    @Override
    public Application<Request, Response> build() throws CreationException {

        exchangeParser(new BasicExchangeParser());
        exceptionHandler(new ExceptionHandler() {
        });

        return super.build();

    }

}
