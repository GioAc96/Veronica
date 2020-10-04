package rocks.gioac96.veronica.core;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RouterTest {

    @AllArgsConstructor
    private static final class TestRequestHandler implements RequestHandler {

        private final String name;

        @Override
        public Response handle(Request request) {

            return Response.builder()
                .httpStatus(HttpStatus.OK)
                .body(name)
                .build();

        }

    }

    private static final Response defaultResponse = Response.builder()
        .httpStatus(HttpStatus.NOT_FOUND)
        .build();
    private static final RequestHandler defaultRequestHandler = request -> defaultResponse;

    private static void assertSameResponse(
        TestRequestHandler testRequestHandler,
        Response response
    ) {

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertArrayEquals(testRequestHandler.handle(null).getBody(), response.getBody());

    }

    private static void assertNoRouteFound(
        Response response
    ) {

        assertSame(defaultResponse, response);

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

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .route(rm(""), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

            assertSameResponse(r1, router.handle(mockRequest(httpMethod, "")));
            assertSameResponse(r1, router.handle(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootNoMethodSlash() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .route(rm("/"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

            assertSameResponse(r1, router.handle(mockRequest(httpMethod, "")));
            assertSameResponse(r1, router.handle(mockRequest(httpMethod, "/")));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(httpMethod, invalidPath)))
            );

        });

    }

    @Test
    void testRootMethod() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod -> {

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            Router router = Router.builder()
                .route(rm(validMethod, ""), r1)
                .defaultRequestHandler(defaultRequestHandler)
                .build();

            assertSameResponse(r1, router.handle(request));
            assertSameResponse(r1, router.handle(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNoRouteFound(router.handle(requestInvalidMethod));
                    assertNoRouteFound(router.handle(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNoRouteFound(router.handle(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootMethodSlash() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(validMethod -> {

            Request request = mockRequest(validMethod, "");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            Router router = Router.builder()
                .route(rm(validMethod, "/"), r1)
                .defaultRequestHandler(defaultRequestHandler)
                .build();

            assertSameResponse(r1, router.handle(request));
            assertSameResponse(r1, router.handle(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "");
                    Request requestSlashInvalidMethod = mockRequest(invalidMethod, request.getPath() + "/");

                    assertNoRouteFound(router.handle(requestInvalidMethod));
                    assertNoRouteFound(router.handle(requestSlashInvalidMethod));

                    Arrays.stream(invalidPaths).forEach(invalidPath ->
                        assertNoRouteFound(router.handle(mockRequest(invalidMethod, invalidPath)))
                    );

                });

        });

    }

    @Test
    void testRootStarNoMethod() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Router router = Router.builder()
            .route(rm("*"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            Arrays.stream(randomPaths).forEach(path ->
                assertSameResponse(r1, router.handle(mockRequest(httpMethod, path)))
            )
        );

    }

    @Test
    void testRootStarNoMethodSlash() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        String[] randomPaths = new String[]{

            "",
            "/",
            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Router router = Router.builder()
            .route(rm("/*"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            Arrays.stream(randomPaths).forEach(path ->
                assertSameResponse(r1, router.handle(mockRequest(httpMethod, path)))
            )
        );

    }

    @Test
    void testStarNoMethod() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .route(rm("/this/path/is/valid/*"), r1)
            .defaultRequestHandler(defaultRequestHandler)
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

        Arrays.stream(validPaths).forEachOrdered(validPath ->

            Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod ->

                assertSameResponse(r1, router.handle(mockRequest(httpMethod, validPath)))

            )

        );

        Arrays.stream(invalidPaths).forEachOrdered(invalidPath ->

            Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod ->

                assertNoRouteFound(router.handle(mockRequest(httpMethod, invalidPath)))

            )

        );

    }

    @Test
    void testStarMethod() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

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
                .route(rm(validMethod, "/this/path/is/valid/*"), r1)
                .defaultRequestHandler(defaultRequestHandler)
                .build();

            Arrays.stream(validPaths).forEachOrdered(validPath -> {

                assertSameResponse(r1, router.handle(mockRequest(validMethod, validPath)));

                Arrays.stream(HttpMethod.values())
                    .filter(invalidMethod -> invalidMethod != validMethod)
                    .forEachOrdered(invalidMethod ->

                        assertNoRouteFound(router.handle(mockRequest(invalidMethod, validPath)))

                    );

            });

            Arrays.stream(invalidPaths).forEachOrdered(invalidPath ->

                Arrays.stream(HttpMethod.values())
                    .forEachOrdered(method ->

                        assertNoRouteFound(router.handle(mockRequest(method, invalidPath)))

                    )

            );

        });


    }

    @Test
    void testStarNotOverrides() {

        TestRequestHandler r1 = new TestRequestHandler("r1");
        TestRequestHandler r2 = new TestRequestHandler("r2");
        TestRequestHandler r3 = new TestRequestHandler("r3");

        Router router = Router.builder()
            .route(rm("/valid/route1"), r1)
            .route(rm("/valid/*"), r2)
            .route(rm("/valid/route3"), r3)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Arrays.stream(HttpMethod.values()).forEachOrdered(httpMethod -> {

            assertSameResponse(r1, router.handle(mockRequest(httpMethod, "/valid/route1")));

            assertSameResponse(r2, router.handle(mockRequest(httpMethod, "/valid/route2")));
            assertSameResponse(r2, router.handle(mockRequest(httpMethod, "/valid/random")));
            assertSameResponse(r2, router.handle(mockRequest(httpMethod, "/valid/random/deep")));
            assertSameResponse(r2, router.handle(mockRequest(httpMethod, "/valid/route1/deep")));

            assertSameResponse(r3, router.handle(mockRequest(httpMethod, "/valid/route3")));

            assertNoRouteFound(router.handle(mockRequest(httpMethod, "/")));
            assertNoRouteFound(router.handle(mockRequest(httpMethod, "/invalid")));
            assertNoRouteFound(router.handle(mockRequest(httpMethod, "/invalid/route1")));
            assertNoRouteFound(router.handle(mockRequest(httpMethod, "/invalid/route2")));
            assertNoRouteFound(router.handle(mockRequest(httpMethod, "/invalid/route3")));

        });

    }

    @Test
    void testCondition() {

        TestRequestHandler secureRoute = new TestRequestHandler("secure");
        TestRequestHandler nonSecureRoute = new TestRequestHandler("nonsecure");

        Router router = Router.builder()
            .route(rm("/home", Request::isSecure), secureRoute)
            .route(rm("/home", request -> !request.isSecure()), nonSecureRoute)
            .defaultRequestHandler(defaultRequestHandler)
            .build();


        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSameResponse(secureRoute, router.handle(secure));
        assertSameResponse(nonSecureRoute, router.handle(nonSecure));

    }

    @Test
    void testStarCondition() {

        TestRequestHandler secureRoute = new TestRequestHandler("secure");
        TestRequestHandler nonSecureRoute = new TestRequestHandler("nonsecure");

        Router router = Router.builder()
            .route(rm("/home/*", Request::isSecure), secureRoute)
            .route(rm("/home/*", request -> !request.isSecure()), nonSecureRoute)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSameResponse(secureRoute, router.handle(secure));
        assertSameResponse(nonSecureRoute, router.handle(nonSecure));

    }

    @Test
    void testVariablePathPart() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .route(rm("{devName}"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Request mockRequest = mockRequest(HttpMethod.GET, "/giorgio");

        when(mockRequest.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSameResponse(r1, router.handle(mockRequest));
        assertEquals("giorgio", mockRequest.getVariablePathParts().get("devName"));

    }

    @Test
    void testVariablePathPartInterference() {

        TestRequestHandler r1 = new TestRequestHandler("r1");
        TestRequestHandler r2 = new TestRequestHandler("r2");

        Router router = Router.builder()
            .route(rm("root/{devName}"), r1)
            .route(rm("root/r2"), r2)
            .defaultRequestHandler(defaultRequestHandler)
            .build();


        Request req1 = mockRequest(HttpMethod.GET, "/root/giorgio");
        Request req2 = mockRequest(HttpMethod.GET, "/root/r2");

        when(req1.getVariablePathParts()).thenReturn(new HashMap<>());
        when(req2.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSameResponse(r1, router.handle(req1));
        assertEquals("giorgio", req1.getVariablePathParts().get("devName"));

        assertSameResponse(r2, router.handle(req2));
        assertEquals(0, req2.getVariablePathParts().size());

    }

    @Test
    void testMultipleVariablePathParts() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .route(rm("{param1}/{param2}/{param3}"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .build();

        Request req1 = mockRequest(HttpMethod.GET, "/p1/p2/p3");
        when(req1.getVariablePathParts()).thenReturn(new HashMap<>());

        Request req2 = mockRequest(HttpMethod.GET, "/var1/var2/var3");
        when(req2.getVariablePathParts()).thenReturn(new HashMap<>());

        Request req3 = mockRequest(HttpMethod.GET, "/veronica/gioac96/rocks");
        when(req3.getVariablePathParts()).thenReturn(new HashMap<>());

        assertSameResponse(r1, router.handle(req1));
        assertSameResponse(r1, router.handle(req2));
        assertSameResponse(r1, router.handle(req3));

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