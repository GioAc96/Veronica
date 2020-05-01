package rocks.gioac96.veronica;

import javax.net.ssl.SSLContext;
import rocks.gioac96.veronica.Application.ApplicationBuilder;
import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Router;

/**
 * Application factory.
 *
 * @param <Q> Request type
 * @param <S> Response type
 */
public abstract class ApplicationFactory<Q extends Request, S extends Response>
    extends ApplicationBuilder<Q, S>
    implements ConfigurableFactory<Application<Q, S>> {

    protected ApplicationBuilder<Q, S> exchangeParser(Factory<ExchangeParser<Q>> exchangeParserFactory)
        throws CreationException {

        return exchangeParser(exchangeParserFactory.build());

    }

    protected ApplicationBuilder<Q, S> exceptionHandler(Factory<ExceptionHandler> exceptionHandlerFactory)
        throws CreationException {

        return exceptionHandler(exceptionHandlerFactory.build());

    }


    protected ApplicationBuilder<Q, S> router(Factory<Router<Q, S>> routerFactory)
        throws CreationException {

        return router(routerFactory.build());

    }

    protected ApplicationBuilder<Q, S> server(Factory<? extends Server> serverFactory) {

        return server(serverFactory.build());

    }


    @Override
    public Application<Q, S> build() throws CreationException {

        configure();

        return super.build();

    }

}
