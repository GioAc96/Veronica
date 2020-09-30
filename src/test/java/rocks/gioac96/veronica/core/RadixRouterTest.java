package rocks.gioac96.veronica.core;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RadixRouterTest {

    private RadixRouter radixRouter;

    private static final class PathMethodTuple{

        private final String path;
        private final HttpMethod[] httpMethods;

        public PathMethodTuple(String path, HttpMethod... httpMethods) {
            this.path = path;
            this.httpMethods = httpMethods;
        }
    }

    @BeforeEach
    void setUp() {

        radixRouter = new RadixRouter();

    }


    @Test
    void testRootNoMethod() {

        String value = "val";

        radixRouter.register("", value);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            assertSame(value, radixRouter.getValue("", httpMethod))
        );
        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            assertSame(value, radixRouter.getValue("/", httpMethod))
        );

    }

    @Test
    void testRootNoMethodSlash() {

        String value = "val";

        radixRouter.register("/", value);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            assertSame(value, radixRouter.getValue("", httpMethod))
        );
        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
            assertSame(value, radixRouter.getValue("/", httpMethod))
        );

    }

    @Test
    void testRootMethod() {

        String value = "val";

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

                setUp();

                radixRouter.register("", httpMethod, value);

                Arrays.stream(HttpMethod.values()).filter(httpMethod1 -> httpMethod != httpMethod1).forEach(httpMethod1 -> {

                    assertNull(radixRouter.getValue("", httpMethod1));
                    assertNull(radixRouter.getValue("/", httpMethod1));

                });

                assertSame(value, radixRouter.getValue("", httpMethod));
                assertSame(value, radixRouter.getValue("/", httpMethod));

            });

    }

    @Test
    void testLeafNoMethod() {

        String value = "thisisthevalue";

        radixRouter.register("this/is/a/path", value);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> assertSame(value, radixRouter.getValue("/this/is/a/path", httpMethod)));
        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> assertSame(value, radixRouter.getValue("this/is/a/path", httpMethod)));

    }
    @Test
    void testLeafNoMethodSlash() {

        String value = "thisisthevalue";

        radixRouter.register("/this/is/a/path", value);

        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> assertSame(value, radixRouter.getValue("/this/is/a/path", httpMethod)));
        Arrays.stream(HttpMethod.values()).forEach(httpMethod -> assertSame(value, radixRouter.getValue("this/is/a/path", httpMethod)));

    }

    @Test
    void testMultipleNoMethod() {

        Map<String, String> values = new HashMap<>(){{
            put("/", "root");
            put("/child", "child 1");
            put("child/2", "child 2");
            put("/child/3", "child 3");
            put("child/another/deep/path", "child 4");
        }};

        values.forEach((path, value) -> radixRouter.register(path, value));

        values.forEach((path, value) -> {

            Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

                assertSame(value, radixRouter.getValue(path, httpMethod));

            });

        });

        Arrays.stream(new String[]{
            "test",
            "non-existent",
            "random/path",
            "/childd",
            "/child/azz"
        }).forEach(path -> {

            Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

                assertNull(radixRouter.getValue(path, httpMethod));

            });

        });

    }
    @Test
    void testMultipleWithMethod() {

        Map<PathMethodTuple, String> values = new LinkedHashMap<>(){{
            put(new PathMethodTuple("/", HttpMethod.GET), "v1");
            put(new PathMethodTuple("", HttpMethod.POST, HttpMethod.DELETE), "v2");
            put(new PathMethodTuple("/child", HttpMethod.HEAD), "v3");
            put(new PathMethodTuple("child", HttpMethod.DELETE), "v4");
            put(new PathMethodTuple("this/path/is/really/deep", HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.TRACE), "v5");
            put(new PathMethodTuple("/this/path/is/really/deep", HttpMethod.CONNECT), "v6");
        }};

        values
            .forEach((pathMethodTuple, value) -> {

                radixRouter.register(pathMethodTuple.path, Arrays.asList(pathMethodTuple.httpMethods), value);

            });

        values.forEach((pathMethodTuple, value) -> {

            Arrays.stream(pathMethodTuple.httpMethods).forEach(httpMethod -> {

                assertSame(value, radixRouter.getValue(pathMethodTuple.path, httpMethod));

            });

        });

        Arrays.stream(new String[]{
            "test",
            "non-existent",
            "random/path",
            "/childd",
            "/child/azz",
            "this/path/is/really/deep/and/even/deeper"
        }).forEach(path -> {

            Arrays.stream(HttpMethod.values()).forEach(httpMethod -> {

                assertNull(radixRouter.getValue(path, httpMethod));

            });

        });

    }

}