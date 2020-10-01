package rocks.gioac96.veronica.core;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RadixRouterTest {

    private RadixRouter radixRouter;

    @BeforeEach
    void setUp() {

        radixRouter = new RadixRouter();

    }

    private Request mockRequest(HttpMethod httpMethod, String path) {

        Request mock = Mockito.mock(Request.class);

        when(mock.getHttpMethod()).thenReturn(httpMethod);
        when(mock.getPath()).thenReturn(path);

        return mock;

    }

    @Test
    void testRootNoMethod() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        radixRouter.register("", r1);

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            assertSame(r1, radixRouter.route(mockRequest(httpMethod, "")));
            assertSame(r1, radixRouter.route(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(radixRouter.route(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootNoMethodSlash() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        radixRouter.register("/", r1);

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            assertSame(r1, radixRouter.route(mockRequest(httpMethod, "")));
            assertSame(r1, radixRouter.route(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(radixRouter.route(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootMethod() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod ->{

            setUp();

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            radixRouter.register(validMethod, "", r1);

            assertSame(r1, radixRouter.route(request));
            assertSame(r1, radixRouter.route(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(radixRouter.route(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNull(radixRouter.route(requestInvalidMethod));
                    assertNull(radixRouter.route(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNull(radixRouter.route(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootMethodSlash() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod ->{

            setUp();

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            radixRouter.register(validMethod, "/", r1);

            assertSame(r1, radixRouter.route(request));
            assertSame(r1, radixRouter.route(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(radixRouter.route(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNull(radixRouter.route(requestInvalidMethod));
                    assertNull(radixRouter.route(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNull(radixRouter.route(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootStarNoMethod() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        radixRouter.register("*", r1);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            Arrays.stream(randomPaths).forEach(path ->
                assertSame(r1, radixRouter.route(mockRequest(httpMethod, path)))
            );

        });

    }

    @Test
    void testRootStarNoMethodSlash() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        radixRouter.register("/*", r1);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            Arrays.stream(randomPaths).forEach(path ->
                assertSame(r1, radixRouter.route(mockRequest(httpMethod, path)))
            );

        });

    }

    @Test
    void testStarNoMethod() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        radixRouter.register("/this/path/is/valid/*", r1);

        String[] validPaths = new String[]{

            "/this/path/is/valid",
            "this/path/is/valid",
            "/this/path/is/valid/and/this/one/too",
            "this/path/is/valid/and/this/one/too",
            "/this/path/is/valid/samehere",
            "this/path/is/valid/samehere",

        };
        String[] invalidPaths = new String[]{

            "/this",
            "/this/path",
            "/this/path/is",
            "/this/path/is/vali",
            "/other/path",
            "/other",
            "/other/path/is/valid",
            "/other/path/is/valid/and/this/one/too",
            "/other/path/is/valid/samehere",
            "this",
            "this/path",
            "this/path/is",
            "this/path/is/vali",
            "other/path",
            "other",
            "other/path/is/valid",
            "other/path/is/valid/and/this/one/too",
            "other/path/is/valid/samehere",

        };

        Arrays.stream(validPaths).forEachOrdered(validPath -> {

            Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

                assertSame(r1, radixRouter.route(mockRequest(httpMethod, validPath)));

            });

        });

        Arrays.stream(invalidPaths).forEachOrdered(invalidPath -> {

            Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

                assertNull(radixRouter.route(mockRequest(httpMethod, invalidPath)));

            });

        });

    }
    @Test
    void testStarMethod() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");

        String[] validPaths = new String[]{

            "/this/path/is/valid",
            "this/path/is/valid",
            "/this/path/is/valid/and/this/one/too",
            "this/path/is/valid/and/this/one/too",
            "/this/path/is/valid/samehere",
            "this/path/is/valid/samehere",

        };
        String[] invalidPaths = new String[]{

            "/this",
            "/this/path",
            "/this/path/is",
            "/this/path/is/vali",
            "/other/path",
            "/other",
            "/other/path/is/valid",
            "/other/path/is/valid/and/this/one/too",
            "/other/path/is/valid/samehere",
            "this",
            "this/path",
            "this/path/is",
            "this/path/is/vali",
            "other/path",
            "other",
            "other/path/is/valid",
            "other/path/is/valid/and/this/one/too",
            "other/path/is/valid/samehere",

        };

        Arrays.stream(HttpMethod.values()).forEachOrdered(validMethod -> {

            setUp();

            radixRouter.register(validMethod, "/this/path/is/valid/*", r1);

            Arrays.stream(validPaths).forEachOrdered(validPath -> {

                assertSame(r1, radixRouter.route(mockRequest(validMethod, validPath)));

                Arrays.stream(HttpMethod.values())
                    .filter(invalidMethod -> invalidMethod != validMethod)
                    .forEachOrdered(invalidMethod -> {

                        assertNull(radixRouter.route(mockRequest(invalidMethod, validPath)));

                });

            });

            Arrays.stream(invalidPaths).forEachOrdered(invalidPath -> {

                Arrays.stream(HttpMethod.values())
                    .forEachOrdered(method -> {

                        assertNull(radixRouter.route(mockRequest(method, invalidPath)));

                    });

            });

        });


    }

    @Test
    void testStarNotOverrides() {

        RadixRouter.Route r1 = new RadixRouter.Route("r1");
        RadixRouter.Route r2 = new RadixRouter.Route("r2");
        RadixRouter.Route r3 = new RadixRouter.Route("r3");

        radixRouter.register("/valid/route1", r1);
        radixRouter.register("/valid/*", r2);
        radixRouter.register("/valid/route3", r3);

        Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

            assertSame(r1, radixRouter.route(mockRequest(httpMethod, "/valid/route1")));

            assertSame(r2, radixRouter.route(mockRequest(httpMethod, "/valid/route2")));
            assertSame(r2, radixRouter.route(mockRequest(httpMethod, "/valid/random")));
            assertSame(r2, radixRouter.route(mockRequest(httpMethod, "/valid/random/deep")));
            assertSame(r2, radixRouter.route(mockRequest(httpMethod, "/valid/route1/deep")));

            assertSame(r3, radixRouter.route(mockRequest(httpMethod, "/valid/route3")));

            assertNull(radixRouter.route(mockRequest(httpMethod, "/")));
            assertNull(radixRouter.route(mockRequest(httpMethod, "/invalid")));
            assertNull(radixRouter.route(mockRequest(httpMethod, "/invalid/route1")));
            assertNull(radixRouter.route(mockRequest(httpMethod, "/invalid/route2")));
            assertNull(radixRouter.route(mockRequest(httpMethod, "/invalid/route3")));

        });

    }

    @Test
    void testCondition() {

        RadixRouter.Route secureRoute = new RadixRouter.Route("secure");
        RadixRouter.Route nonSecureRoute = new RadixRouter.Route("nonsecure");

        radixRouter.register("/home", Request::isSecure, secureRoute);
        radixRouter.register("/home", request -> ! request.isSecure(), nonSecureRoute);

        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSame(secureRoute, radixRouter.route(secure));
        assertSame(nonSecureRoute, radixRouter.route(nonSecure));

    }

    @Test
    void testStarCondition() {

        RadixRouter.Route secureRoute = new RadixRouter.Route("secure");
        RadixRouter.Route nonSecureRoute = new RadixRouter.Route("nonsecure");

        radixRouter.register("/home/*", Request::isSecure, secureRoute);
        radixRouter.register("/home/*", request -> ! request.isSecure(), nonSecureRoute);

        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSame(secureRoute, radixRouter.route(secure));
        assertSame(nonSecureRoute, radixRouter.route(nonSecure));

    }

}