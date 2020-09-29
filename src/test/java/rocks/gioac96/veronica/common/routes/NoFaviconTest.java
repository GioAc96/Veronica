package rocks.gioac96.veronica.common.routes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import rocks.gioac96.veronica.common.request_matchers.Favicon;
import rocks.gioac96.veronica.core.Route;

class NoFaviconTest {

    private final Route noFaviconRoute = new NoFavicon().build();

    @Test
    void testRequestMatcher() {

        assertSame(new Favicon().build(), noFaviconRoute.getRequestMatcher());

    }

    @Test
    void testRequestHandler() {

        Route notFound = new NotFound().build();

        assertSame(notFound.getRequestHandler(), noFaviconRoute.getRequestHandler());

    }

}