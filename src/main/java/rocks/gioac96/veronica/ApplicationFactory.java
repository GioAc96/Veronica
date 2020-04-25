package rocks.gioac96.veronica;

import rocks.gioac96.veronica.Application.ApplicationBuilder;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.http.ExceptionHandler;
import rocks.gioac96.veronica.http.ExchangeParser;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Router;

public abstract class ApplicationFactory<Q extends Request, S extends Response>

    extends ApplicationBuilder<Q, S>
    implements Factory<Application<Q, S>> {

    protected ApplicationBuilder<Q, S> exchangeParser(Factory<ExchangeParser<Q>> exchangeParserFactory) throws CreationException {

        return exchangeParser(exchangeParserFactory.build());

    }

    protected ApplicationBuilder<Q, S> exceptionHandler(Factory<ExceptionHandler> exceptionHandlerFactory) throws CreationException {

        return exceptionHandler(exceptionHandlerFactory.build());

    }


    protected ApplicationBuilder<Q, S> port(Factory<Integer> portFactory) throws CreationException {

        return port(portFactory.build());

    }

    protected ApplicationBuilder<Q, S> router(Factory<Router<Q, S>> routerFactory) throws CreationException {

        return router(routerFactory.build());

    }
    
    @Override
    public Application<Q, S> build() throws CreationException {

        configure();

        return super.build();

    }

}
