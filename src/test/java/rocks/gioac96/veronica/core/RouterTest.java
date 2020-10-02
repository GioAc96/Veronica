package rocks.gioac96.veronica.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RouterTest {

    @AllArgsConstructor
    private static final class Route implements RequestHandler {

        private final String name;

        @Override
        public Response handle(Request request) {
            return null;
        }
    }

    private static RequestMatcher rm(String pathPattern) {

        return RequestMatcher.builder()
            .pathPattern(pathPattern)
            .build();

    }

    private static RequestMatcher rm(HttpMethod httpMethod, String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(httpMethod)
            .pathPattern(pathPattern)
            .build();

    }
    private static RequestMatcher rm(HttpMethod httpMethod, String pathPattern, Predicate<Request> condition) {

        return RequestMatcher.builder()
            .httpMethod(httpMethod)
            .pathPattern(pathPattern)
            .condition(condition)
            .build();

    }
    private static RequestMatcher rm(String pathPattern, Predicate<Request> condition) {

        return RequestMatcher.builder()
            .pathPattern(pathPattern)
            .condition(condition)
            .build();

    }

    private Request mockRequest(HttpMethod httpMethod, String path) {

        Request mock = Mockito.mock(Request.class);

        when(mock.getHttpMethod()).thenReturn(httpMethod);
        when(mock.getPath()).thenReturn(path);

        return mock;

    }

    @Test
    void testRootNoMethod() {

        Route r1 = new Route("r1");

        Router router = Router.builder()
            .register(rm(""), r1)
            .build();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            assertSame(r1, router.route(mockRequest(httpMethod, "")));
            assertSame(r1, router.route(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(router.route(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootNoMethodSlash() {

        Route r1 = new Route("r1");

        Router router = Router.builder()
            .register(rm("/"), r1)
            .build();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            assertSame(r1, router.route(mockRequest(httpMethod, "")));
            assertSame(r1, router.route(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(router.route(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootMethod() {

        Route r1 = new Route("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod ->{

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            Router router = Router.builder()
                .register(rm(validMethod, ""), r1)
                .build();

            assertSame(r1, router.route(request));
            assertSame(r1, router.route(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(router.route(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNull(router.route(requestInvalidMethod));
                    assertNull(router.route(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNull(router.route(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootMethodSlash() {

        Route r1 = new Route("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod ->{

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            Router router = Router.builder()
                .register(rm(validMethod, "/"), r1)
                .build();

            assertSame(r1, router.route(request));
            assertSame(r1, router.route(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNull(router.route(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNull(router.route(requestInvalidMethod));
                    assertNull(router.route(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNull(router.route(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootStarNoMethod() {

        Route r1 = new Route("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Router router = Router.builder()
            .register(rm("*"), r1)
            .build();

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            Arrays.stream(randomPaths).forEach(path ->
                assertSame(r1, router.route(mockRequest(httpMethod, path)))
            );

        });

    }

    @Test
    void testRootStarNoMethodSlash() {

        Route r1 = new Route("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Router router = Router.builder()
            .register(rm("/*"), r1)
            .build();

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->{

            Arrays.stream(randomPaths).forEach(path ->
                assertSame(r1, router.route(mockRequest(httpMethod, path)))
            );

        });

    }

    @Test
    void testStarNoMethod() {

        Route r1 = new Route("r1");

        Router router = Router.builder()
            .register(rm("/this/path/is/valid/*"), r1)
            .build();

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

                assertSame(r1, router.route(mockRequest(httpMethod, validPath)));

            });

        });

        Arrays.stream(invalidPaths).forEachOrdered(invalidPath -> {

            Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

                assertNull(router.route(mockRequest(httpMethod, invalidPath)));

            });

        });

    }
    @Test
    void testStarMethod() {

        Route r1 = new Route("r1");

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

            Router router = Router.builder()
                .register(rm("/this/path/is/valid/*"), r1)
                .build();

            Arrays.stream(validPaths).forEachOrdered(validPath -> {

                assertSame(r1, router.route(mockRequest(validMethod, validPath)));

                Arrays.stream(HttpMethod.values())
                    .filter(invalidMethod -> invalidMethod != validMethod)
                    .forEachOrdered(invalidMethod -> {

                        assertNull(router.route(mockRequest(invalidMethod, validPath)));

                });

            });

            Arrays.stream(invalidPaths).forEachOrdered(invalidPath -> {

                Arrays.stream(HttpMethod.values())
                    .forEachOrdered(method -> {

                        assertNull(router.route(mockRequest(method, invalidPath)));

                    });

            });

        });


    }

    @Test
    void testStarNotOverrides() {

        Route r1 = new Route("r1");
        Route r2 = new Route("r2");
        Route r3 = new Route("r3");

        Router router = Router.builder()
            .register(rm("/valid/route1"), r1)
            .register(rm("/valid/*"), r2)
            .register(rm("/valid/route3"), r3)
            .build();

        Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

            assertSame(r1, router.route(mockRequest(httpMethod, "/valid/route1")));

            assertSame(r2, router.route(mockRequest(httpMethod, "/valid/route2")));
            assertSame(r2, router.route(mockRequest(httpMethod, "/valid/random")));
            assertSame(r2, router.route(mockRequest(httpMethod, "/valid/random/deep")));
            assertSame(r2, router.route(mockRequest(httpMethod, "/valid/route1/deep")));

            assertSame(r3, router.route(mockRequest(httpMethod, "/valid/route3")));

            assertNull(router.route(mockRequest(httpMethod, "/")));
            assertNull(router.route(mockRequest(httpMethod, "/invalid")));
            assertNull(router.route(mockRequest(httpMethod, "/invalid/route1")));
            assertNull(router.route(mockRequest(httpMethod, "/invalid/route2")));
            assertNull(router.route(mockRequest(httpMethod, "/invalid/route3")));

        });

    }

    @Test
    void testCondition() {

        Route secureRoute = new Route("secure");
        Route nonSecureRoute = new Route("nonsecure");

        Router router = Router.builder()
            .register(rm("/home", Request::isSecure), secureRoute)
            .register(rm("/home", request -> ! request.isSecure()), nonSecureRoute)
            .build();


        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSame(secureRoute, router.route(secure));
        assertSame(nonSecureRoute, router.route(nonSecure));

    }

    @Test
    void testStarCondition() {

        Route secureRoute = new Route("secure");
        Route nonSecureRoute = new Route("nonsecure");

        Router router = Router.builder()
            .register(rm("/home/*", Request::isSecure), secureRoute)
            .register(rm("/home/*", request -> ! request.isSecure()), nonSecureRoute)
            .build();

        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSame(secureRoute, router.route(secure));
        assertSame(nonSecureRoute, router.route(nonSecure));

    }

    @Test
    void testVariablePathPart() {

        Route r1 = new Route("r1");

        Router router = Router.builder()
            .register(rm("{devName}"), r1)
            .build();

        Request mockRequest = mockRequest(HttpMethod.GET, "/giorgio");

        when(mockRequest.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSame(r1, router.route(mockRequest));
        assertEquals("giorgio", mockRequest.getVariablePathParts().get("devName"));

    }

    @Test
    void testVariablePathPartInterference() {

        Route r1 = new Route("r1");
        Route r2 = new Route("r2");

        Router router = Router.builder()
            .register(rm("root/{devName}"), r1)
            .register(rm("root/r2"), r2)
            .build();


        Request req1 = mockRequest(HttpMethod.GET, "/root/giorgio");
        Request req2 = mockRequest(HttpMethod.GET, "/root/r2");

        when(req1.getVariablePathParts()).thenReturn(new HashMap<>());
        when(req2.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSame(r1, router.route(req1));
        assertEquals("giorgio", req1.getVariablePathParts().get("devName"));

        assertSame(r2, router.route(req2));
        assertEquals(0, req2.getVariablePathParts().size());

    }

    @Test
    void testMultipleVariablePathParts() {

        Route r1 = new Route("r1");

        Router router = Router.builder()
            .register(rm("{param1}/{param2}/{param3}"), r1)
            .build();

        Request req1 = mockRequest(HttpMethod.GET, "/p1/p2/p3");
        when(req1.getVariablePathParts()).thenReturn(new HashMap<>());

        Request req2 = mockRequest(HttpMethod.GET, "/var1/var2/var3");
        when(req2.getVariablePathParts()).thenReturn(new HashMap<>());

        Request req3 = mockRequest(HttpMethod.GET, "/veronica/gioac96/rocks");
        when(req3.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSame(r1, router.route(req1));
        assertSame(r1, router.route(req2));
        assertSame(r1, router.route(req3));

        assertEquals("p1", req1.getVariablePathParts().get("param1"));
        assertEquals("p2", req1.getVariablePathParts().get("param2"));
        assertEquals("p3", req1.getVariablePathParts().get("param3"));

        assertEquals("var1", req2.getVariablePathParts().get("param1"));
        assertEquals("var2", req2.getVariablePathParts().get("param2"));
        assertEquals("var3", req2.getVariablePathParts().get("param3"));

        assertEquals("veronica", req3.getVariablePathParts().get("param1"));
        assertEquals("gioac96", req3.getVariablePathParts().get("param2"));
        assertEquals("rocks", req3.getVariablePathParts().get("param3"));

    }

}