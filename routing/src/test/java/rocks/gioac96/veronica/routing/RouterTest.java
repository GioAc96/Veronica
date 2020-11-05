package rocks.gioac96.veronica.routing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rocks.gioac96.veronica.core.HttpMethod;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;

class RouterTest {

    private static final Response defaultResponse = Response.builder()
        .httpStatus(HttpStatus.NOT_FOUND)
        .provide();
    private static final RequestHandler defaultRequestHandler = request -> defaultResponse;

    private static void assertSameResponse(
        TestRequestHandler testRequestHandler,
        Response response
    ) {

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(testRequestHandler.handle(null).getBodyString(), response.getBodyString());

    }

    private static void assertNoRouteFound(
        Response response
    ) {

        assertSame(defaultResponse, response);

    }

    private static RequestMatcher rm(String pathPattern) {

        return RequestMatcher.builder()
            .pathPattern(pathPattern)
            .provide();

    }

    private static RequestMatcher rm(HttpMethod httpMethod, String pathPattern) {

        return RequestMatcher.builder()
            .httpMethod(httpMethod)
            .pathPattern(pathPattern)
            .provide();

    }

    private static RequestMatcher rm(HttpMethod httpMethod, String pathPattern, Predicate<Request> condition) {

        return RequestMatcher.builder()
            .httpMethod(httpMethod)
            .pathPattern(pathPattern)
            .condition(condition)
            .provide();

    }

    private static RequestMatcher rm(String pathPattern, Predicate<Request> condition) {

        return RequestMatcher.builder()
            .pathPattern(pathPattern)
            .condition(condition)
            .provide();

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
            .route(rm("/"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

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
            .provide();

        String[] invalidPaths = new String[]{

            "test",
            "/test",
            "/very/deep/path",
            "very/deep/path",

        };

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

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

            Request request = mockRequest(validMethod, "/");
            Request requestSlash = mockRequest(validMethod, request.getPath() + "/");

            Router router = Router.builder()
                .route(rm(validMethod, "/"), r1)
                .defaultRequestHandler(defaultRequestHandler)
                .provide();

            assertSameResponse(r1, router.handle(request));
            assertSameResponse(r1, router.handle(requestSlash));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "/");

                    assertNoRouteFound(router.handle(requestInvalidMethod));

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

            Request request = mockRequest(validMethod, "/");

            Router router = Router.builder()
                .route(rm(validMethod, "/"), r1)
                .defaultRequestHandler(defaultRequestHandler)
                .provide();

            assertSameResponse(r1, router.handle(request));

            Arrays.stream(invalidPaths).forEach(invalidPath ->
                assertNoRouteFound(router.handle(mockRequest(validMethod, invalidPath)))
            );

            Arrays.stream(HttpMethod.values())
                .filter(invalidMethod -> invalidMethod != validMethod)
                .forEach(invalidMethod -> {

                    Request requestInvalidMethod = mockRequest(invalidMethod, "/");

                    assertNoRouteFound(router.handle(requestInvalidMethod));

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

            "/",
            "/test",
            "/very/deep/path",

        };

        Router router = Router.builder()
            .route(rm("/*"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

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

            "/",
            "/test",
            "/very/deep/path",

        };

        Router router = Router.builder()
            .route(rm("/*"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

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
            .provide();

        String[] validPaths = new String[]{

            "/this/path/is/valid",
            "/this/path/is/valid/and/this/one/too",
            "/this/path/is/valid/samehere",

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
            "/this/path/is/valid/and/this/one/too",
            "/this/path/is/valid/samehere",

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
                .provide();

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
            .provide();

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
            .provide();


        Request nonSecure = mockRequest(HttpMethod.GET, "/home");
        when(nonSecure.isSecure()).thenReturn(false);

        Request secure = mockRequest(HttpMethod.GET, "/home");
        when(secure.isSecure()).thenReturn(true);

        assertSameResponse(secureRoute, router.handle(secure));
        assertSameResponse(nonSecureRoute, router.handle(nonSecure));

    }

    @Test
    void testMultipleConditionsAndPathPatterns() {

        Route secureRoute = Route.builder()
            .requestMatcher(RequestMatcher.builder()
                .pathPattern("/home")
                .condition(Request::isSecure)
                .condition(request -> request.getBody().equals("request is secure"))
                .provide())
            .requestHandler(request -> Response.builder()
                .body("secure route")
                .provide())
            .provide();

        Router router = Router.builder()
            .route(secureRoute)
            .defaultRequestHandler(request -> null)
            .provide();

        Request validRequestHome = Mockito.mock(Request.class);
        when(validRequestHome.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(validRequestHome.getPath()).thenReturn("/home");
        when(validRequestHome.isSecure()).thenReturn(true);
        when(validRequestHome.getBody()).thenReturn("request is secure");

        assertEquals("secure route", router.handle(validRequestHome).getBodyString());

        Request invalidRequestHomeNonSecure = Mockito.mock(Request.class);
        when(invalidRequestHomeNonSecure.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(invalidRequestHomeNonSecure.getPath()).thenReturn("/home");
        when(invalidRequestHomeNonSecure.isSecure()).thenReturn(false);
        when(invalidRequestHomeNonSecure.getBody()).thenReturn("request is secure");

        assertNull(router.handle(invalidRequestHomeNonSecure));

        Request invalidRequestHomeWrongBody = Mockito.mock(Request.class);
        when(invalidRequestHomeWrongBody.getHttpMethod()).thenReturn(HttpMethod.GET);
        when(invalidRequestHomeWrongBody.getPath()).thenReturn("/home");
        when(invalidRequestHomeWrongBody.isSecure()).thenReturn(true);
        when(invalidRequestHomeWrongBody.getBody()).thenReturn("hello there");

        assertNull(router.handle(invalidRequestHomeWrongBody));

    }

    @Test
    void testStarCondition() {

        TestRequestHandler secureRoute = new TestRequestHandler("secure");
        TestRequestHandler nonSecureRoute = new TestRequestHandler("nonsecure");

        Router router = Router.builder()
            .route(rm("/home/*", Request::isSecure), secureRoute)
            .route(rm("/home/*", request -> !request.isSecure()), nonSecureRoute)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

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
            .provide();

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
            .provide();


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
            .provide();

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

    @Test
    void testRoutingGuardCatch() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Response routingGuardResponse = Response.builder().provide();

        Router router = Router.builder()
            .routingGuard(request -> routingGuardResponse)
            .route(rm(""), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

        assertSame(routingGuardResponse, router.handle(mockRequest(HttpMethod.GET, "")));

    }

    @Test
    void testRoutingGuardPass() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Router router = Router.builder()
            .routingGuard(request -> null)
            .route(rm("/"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

        assertSameResponse(r1, router.handle(mockRequest(HttpMethod.GET, "/")));

    }

    @Test
    void testConditionalRoutingGuard() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Response routingGuardResponse = Response.builder().provide();

        Router router = Router.builder()
            .routingGuard(request -> request.getHttpMethod().equals(HttpMethod.GET) ? routingGuardResponse : null)
            .route(rm(""), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

        assertSame(routingGuardResponse, router.handle(mockRequest(HttpMethod.GET, "/")));
        assertSameResponse(r1, router.handle(mockRequest(HttpMethod.POST, "/")));

    }

    @Test
    void testMultipleRoutingGuards() {

        TestRequestHandler r1 = new TestRequestHandler("r1");

        Response routingGuardResponseGet = Response.builder().provide();
        Response routingGuardResponsePost = Response.builder().provide();

        Router router = Router.builder()
            .routingGuard(request -> request.getHttpMethod().equals(HttpMethod.GET) ? routingGuardResponseGet : null)
            .routingGuard(request -> request.getHttpMethod().equals(HttpMethod.POST) ? routingGuardResponsePost : null)
            .route(rm(HttpMethod.PUT, "/"), r1)
            .defaultRequestHandler(defaultRequestHandler)
            .provide();

        assertSame(routingGuardResponseGet, router.handle(mockRequest(HttpMethod.GET, "/")));
        assertSameResponse(r1, router.handle(mockRequest(HttpMethod.PUT, "/")));
        assertSame(routingGuardResponsePost, router.handle(mockRequest(HttpMethod.POST, "/")));

    }

    @Test
    void testRoutingPrefix() {

        String prefix = "/prefix";

        Router router = Router.builder()
            .pathPrefix(prefix)
            .route(
                RequestMatcher.builder()
                    .httpMethod(HttpMethod.GET)
                    .pathPattern("/giorgio")
                    .provide(),
                request -> Response.builder()
                    .body("routed")
                    .provide()
            )
            .defaultRequestHandler(
                request -> null
            )
            .provide();

        assertEquals(
            "routed",
            router.handle(mockRequest(HttpMethod.GET, "/prefix/giorgio")).getBodyString()
        );

        assertNull(router.handle(mockRequest(HttpMethod.GET, "/")));
        assertNull(router.handle(mockRequest(HttpMethod.GET, "/giorgio")));
        assertNull(router.handle(mockRequest(HttpMethod.GET, "/prefix")));
        assertNull(router.handle(mockRequest(HttpMethod.GET, "/other/path")));
        assertNull(router.handle(mockRequest(HttpMethod.GET, "/prefixlong/giorgio")));
        assertNull(router.handle(mockRequest(HttpMethod.POST, "/prefix/giorgio")));


    }

    @Test
    void testCompositeRouters() {

        Router v1 = Router.builder()
            .pathPrefix("/v1")
            .route("/v1path", request -> Response.builder()
                .body("v1")
                .provide())
            .defaultRequestHandler(request -> null)
            .provide();

        Router v2 = Router.builder()
            .pathPrefix("/v2")
            .route("/v2path", request -> Response.builder()
                .body("v2")
                .provide())
            .defaultRequestHandler(request -> null)
            .provide();

        Router root = Router.builder()
            .route("/v1/*", v1)
            .route("/v2/*", v2)
            .defaultRequestHandler(request -> null)
            .provide();

        Request v1Req = mockRequest(HttpMethod.GET, "/v1/v1path");
        Request v2Req = mockRequest(HttpMethod.GET, "/v2/v2path");

        Request[] invalid = {
            mockRequest(HttpMethod.GET, "/v1/v2path"),
            mockRequest(HttpMethod.GET, "/v2/v1path"),
            mockRequest(HttpMethod.GET, "/v1/other"),
            mockRequest(HttpMethod.GET, "/v2/other"),
            mockRequest(HttpMethod.GET, "/other"),
            mockRequest(HttpMethod.GET, "/")
        };

        assertEquals("v1", root.handle(v1Req).getBodyString());
        assertEquals("v2", root.handle(v2Req).getBodyString());

        for (Request invalidRequest : invalid) {

            assertNull(root.handle(invalidRequest));

        }

    }

    @AllArgsConstructor
    private static final class TestRequestHandler implements RequestHandler {

        private final String name;

        @Override
        public Response handle(Request request) {

            return Response.builder()
                .httpStatus(HttpStatus.OK)
                .body(name)
                .provide();

        }

    }

}