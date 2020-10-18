package rocks.gioac96.veronica.validation.common.routes;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.validation.common.request_handlers.NotFound;
import rocks.gioac96.veronica.validation.common.request_matchers.Favicon;
import rocks.gioac96.veronica.core.RequestHandler;

class NoFaviconTest {

    @Test
    void testRequestMatcher() {

        assertSame(new Favicon().provide(), new NoFavicon().provide().getRequestMatcher());

    }

    @Test
    void testRequestHandler() {

        RequestHandler expected = new NotFound().provide();

        RequestHandler noFaviconRouteRequestHandler = new NoFavicon().provide().getRequestHandler();

        assertSame(expected, noFaviconRouteRequestHandler);

    }

}