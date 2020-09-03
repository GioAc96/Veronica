package rocks.gioac96.veronica;

import rocks.gioac96.veronica.Application.ApplicationBuilder;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.routing.Router;

/**
 * Application factory.
 *
 */
public abstract class ApplicationFactory
    extends ApplicationBuilder
    implements ConfigurableFactory<Application> {

    protected ApplicationBuilder exchangeParser(Factory<ExchangeParser> exchangeParserFactory) {

        return exchangeParser(exchangeParserFactory.build());

    }

    protected ApplicationBuilder exceptionHandler(Factory<ExceptionHandler> exceptionHandlerFactory) {

        return exceptionHandler(exceptionHandlerFactory.build());

    }


    protected ApplicationBuilder router(Factory<Router> routerFactory) {

        return router(routerFactory.build());

    }

    protected ApplicationBuilder server(Factory<? extends Server> serverFactory) {

        return server(serverFactory.build());

    }

    @Override
    public Application build() throws CreationException {

        configure();

        return super.build();

    }

}
