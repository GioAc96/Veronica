package rocks.gioac96.veronica.session.server_side;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.session.SessionStorage;

class ServerSideSessionStorageTest {

    ServerSideSessionStorage<String> sessionStorage;

    Request requestMock;

    @BeforeEach
    void setup() {

        sessionStorage = new ServerSideSessionStorage<>();
        requestMock = mock(Request.class);

    }

    @Test
    void storeSessionDataAndRetrieve() {

        Response.ResponseBuilder responseBuilder = Response.builder();

        when(requestMock.getCookie()).thenReturn(new HashMap<>());

        sessionStorage.setSessionData(requestMock, responseBuilder, "Veronica");

        Response response = responseBuilder.provide();

        assertEquals(1, response.getCookies().size());

        Request secondRequest = mock(Request.class);
        when(secondRequest.getCookie()).thenReturn(new HashMap<>() {{

            response.getCookies().forEach(setCookieHeader -> {

                put(setCookieHeader.getName(), setCookieHeader.getValue());

            });

        }});

        assertEquals("Veronica", sessionStorage.getSessionData(secondRequest));

    }

}