package rocks.gioac96.veronica.session.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.util.Tuple;
import rocks.gioac96.veronica.session.SessionStorage;

class ApplicationSessionStorageTest {

    ApplicationSessionStorage<String> sessionStorage;

    Request requestMock;

    @BeforeEach
    void setup() {

        sessionStorage = ApplicationSessionStorage.<String>builder().provide();
        requestMock = mock(Request.class);
        when(requestMock.getCookie()).thenReturn(new HashMap<>());

    }

    public static Request responseToRequestWithCookies(Response.ResponseBuilder responseBuilder) {

        return responseToRequestWithCookies(responseBuilder.provide());

    }

    public static Request responseToRequestWithCookies(Response response) {

        Request requestWithCookies = mock(Request.class);

        when(requestWithCookies.getCookie()).thenReturn(new HashMap<>() {{

            response.getCookies().forEach(setCookieHeader -> {

                put(setCookieHeader.getName(), setCookieHeader.getValue());

            });

        }});

        return requestWithCookies;

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "veronica",
        "giorgio",
        "hola"
    })
    void storeSessionDataAndRetrieve(String val) {

        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStorage.setSessionData(requestMock, responseBuilder, val);

        assertEquals(
            val,
            sessionStorage.getSessionData(
                responseToRequestWithCookies(responseBuilder)
            )
        );

    }

    @ParameterizedTest
    @ValueSource(ints = {1000})
    void storeMultipleSessionDataAndRetrieve(int nOfSessions) {

        List<Tuple<Request, String>> expectedSessionData = new LinkedList<>();

        for (int i = 0; i < nOfSessions; i++) {

            Response.ResponseBuilder responseBuilder = Response.builder();

            Request rm = mock(Request.class);

            sessionStorage.setSessionData(rm, responseBuilder, String.valueOf(i));

            expectedSessionData.add(new Tuple<>(responseToRequestWithCookies(responseBuilder), String.valueOf(i)));

        }

        while (! expectedSessionData.isEmpty()) {

            int randomIndex = new Random().nextInt(expectedSessionData.size());

            Tuple<Request, String> randomTuple = expectedSessionData.get(randomIndex);

            assertEquals(
                randomTuple.getSecond(),
                sessionStorage.getSessionData(randomTuple.getFirst())
            );

            expectedSessionData.remove(randomIndex);

        }

    }

}