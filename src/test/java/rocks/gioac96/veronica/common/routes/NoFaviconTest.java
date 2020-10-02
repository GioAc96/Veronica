package rocks.gioac96.veronica.common.routes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.common.request_handlers.NotFound;
import rocks.gioac96.veronica.common.request_matchers.Favicon;
import rocks.gioac96.veronica.core.RequestHandler;

class NoFaviconTest {

    @Test
    void testRequestMatcher() {

        assertSame(new Favicon().build(), new NoFavicon().build().getRequestMatcher());

    }

    @Test
    void testRequestHandler() {

        RequestHandler expected = new NotFound().build();

        RequestHandler noFaviconRouteRequestHandler = new NoFavicon().build().getRequestHandler();

        assertSame(expected, noFaviconRouteRequestHandler);

    }

}