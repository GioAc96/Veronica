package rocks.gioac96.veronica.session.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.concurrency.PriorityFixedThreadPoolExecutor;
import rocks.gioac96.veronica.core.util.Tuple;

class ApplicationSessionStoreTest {

    ApplicationSessionStore<String> sessionStore;

    public static Request requestWithEmptyCookieMap() {

        Request requestMock = mock(Request.class);
        when(requestMock.getCookie()).thenReturn(new HashMap<>());

        return requestMock;

    }

    public static Request responseToRequestWithCookies(Response.ResponseBuilder responseBuilder) {

        return responseToRequestWithCookies(responseBuilder.provide());

    }

    public static Request responseToRequestWithCookies(Response response) {

        Request requestWithCookies = mock(Request.class);

        when(requestWithCookies.getCookie()).thenReturn(new HashMap<>() {{

            response.getHeaders().get("Set-Cookie").forEach(cookie -> {

                put(cookie.split("=")[0], cookie.split("=")[1].split(";")[0]);

            });

        }});

        return requestWithCookies;

    }

    @BeforeEach
    void setup() {

        sessionStore = ApplicationSessionStore.<String>builder().provide();

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "veronica",
        "giorgio",
        "hola"
    })
    void storeSessionDataAndRetrieve(String val) {

        Response.ResponseBuilder responseBuilder = Response.builder();

        Request requestMock = requestWithEmptyCookieMap();

        sessionStore.setSessionData(requestMock, responseBuilder, val);

        assertEquals(
            val,
            sessionStore.getSessionData(
                responseToRequestWithCookies(responseBuilder)
            )
        );

    }

    @ParameterizedTest
    @ValueSource(ints = {1000})
    void testStoreMultipleSessionDataAndRetrieve(int nOfSessions) {

        List<Tuple<Request, String>> expectedSessionData = new LinkedList<>();

        for (int i = 0; i < nOfSessions; i++) {

            Response.ResponseBuilder responseBuilder = Response.builder();

            Request rm = mock(Request.class);

            sessionStore.setSessionData(rm, responseBuilder, String.valueOf(i));

            expectedSessionData.add(new Tuple<>(responseToRequestWithCookies(responseBuilder), String.valueOf(i)));

        }

        while (!expectedSessionData.isEmpty()) {

            int randomIndex = new Random().nextInt(expectedSessionData.size());

            Tuple<Request, String> randomTuple = expectedSessionData.get(randomIndex);

            assertEquals(
                randomTuple.getSecond(),
                sessionStore.getSessionData(randomTuple.getFirst())
            );

            expectedSessionData.remove(randomIndex);

        }

    }

    @ParameterizedTest
    @ValueSource(ints = 1000)
    void testConcurrentMultipleSessions(int nOfSessions) throws InterruptedException {

        ExecutorService ex = PriorityFixedThreadPoolExecutor.builder()
            .provide()
            .getExecutorWithDefaultPriority();

        List<Tuple<Request, String>> expectedSessionData = new LinkedList<>();

        Lock lock = new ReentrantLock();

        for (int i = 0; i < nOfSessions; i++) {

            final String data = String.valueOf(i);

            ex.execute(() -> {

                Request requestMock = requestWithEmptyCookieMap();
                Response.ResponseBuilder responseBuilder = Response.builder();

                sessionStore.setSessionData(
                    requestMock,
                    responseBuilder,
                    data
                );

                try {
                    lock.lock();

                    expectedSessionData.add(new Tuple<>(
                        responseToRequestWithCookies(responseBuilder),
                        data
                    ));

                } finally {

                    lock.unlock();

                }


            });

        }

        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.MINUTES);

        expectedSessionData.forEach(exp -> {

            Request req = exp.getFirst();
            String data = exp.getSecond();

            assertEquals(data, sessionStore.getSessionData(req));

        });

    }

    @Test
    void testSessionExpiration() throws InterruptedException {

        sessionStore = ApplicationSessionStore
            .<String>builder()
            .expirationTime(1)
            .provide();

        Request requestMock = requestWithEmptyCookieMap();
        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStore.setSessionData(requestMock, responseBuilder, "my data");

        Request retrieveSessionRequest = responseToRequestWithCookies(responseBuilder);

        Thread.sleep(sessionStore.getExpirationTime() * 1000 / 2);

        assertEquals("my data", sessionStore.getSessionData(retrieveSessionRequest));

        Thread.sleep((long) (sessionStore.getExpirationTime() * 1000 * 1.1));

        assertNull(sessionStore.getSessionData(retrieveSessionRequest));

    }

    @Test
    void testClearSessionData() {

        sessionStore = ApplicationSessionStore
            .<String>builder()
            .expirationTime(1)
            .provide();

        Request requestMock = requestWithEmptyCookieMap();
        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStore.setSessionData(requestMock, responseBuilder, "my data");

        Request retrieveSessionRequest = responseToRequestWithCookies(responseBuilder);

        assertEquals("my data", sessionStore.getSessionData(retrieveSessionRequest));

        sessionStore.clearSessionData(retrieveSessionRequest);

        assertNull(sessionStore.getSessionData(retrieveSessionRequest));


    }

    @Test
    void testAutomaticallyClearExpiredSessions() throws InterruptedException {

        Request requestMock = requestWithEmptyCookieMap();
        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStore = ApplicationSessionStore
            .<String>builder()
            .expirationTime(1)
            .provide();

        sessionStore.setSessionData(requestMock, responseBuilder, "my data");

        assertEquals(1, sessionStore.getStoredSessionsCount());

        Thread.sleep(1500);

        assertEquals(0, sessionStore.getStoredSessionsCount());

    }

    @Test
    void testOverwriteSession() {

        Request requestMock = requestWithEmptyCookieMap();
        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStore.setSessionData(requestMock, responseBuilder, "1");

        requestMock = responseToRequestWithCookies(responseBuilder);

        assertEquals("1", sessionStore.getSessionData(requestMock));

        responseBuilder = Response.builder();

        sessionStore.setSessionData(requestMock, responseBuilder, "2");

        assertEquals("2", sessionStore.getSessionData(requestMock));

        assertEquals(1, sessionStore.getStoredSessionsCount());

    }

    @Test
    void testClearAll() {

        Request requestMock = requestWithEmptyCookieMap();
        Response.ResponseBuilder responseBuilder = Response.builder();

        sessionStore.setSessionData(requestMock, responseBuilder, "1");

        requestMock = responseToRequestWithCookies(responseBuilder);

        assertEquals("1", sessionStore.getSessionData(requestMock));


        sessionStore.clearAllSessions();

        assertNull(sessionStore.getSessionData(requestMock));

        assertEquals(0, sessionStore.getStoredSessionsCount());

    }

    @Test
    void testInvalidSessionKey() {

        Response.ResponseBuilder responseBuilder = Response.builder();

        Request requestMock = requestWithEmptyCookieMap();

        sessionStore.setSessionData(requestMock, responseBuilder, "test");

        Request requestWithWrongSessionKey = mock(Request.class);

        when(requestWithWrongSessionKey.getCookie()).thenReturn(new HashMap<>() {{

            put(sessionStore.getCookieName(), UUID.randomUUID().toString());

        }});

        assertNull(
            sessionStore.getSessionData(requestWithWrongSessionKey)
        );

    }

    @Test
    void testNoSessionKey() {

        Request requestMock = requestWithEmptyCookieMap();

        assertNull(sessionStore.getSessionData(requestMock));

    }

}