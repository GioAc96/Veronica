package org.gioac96.veronica.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.sun.net.httpserver.Headers;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

class RequestTest {

    @Mock
    private Headers headersMock;

    @Mock
    private URI uriMock;

    static Object[][] parseQueryStringTestParameters() {

        Object[][] result = {
            {
                "param_1=test",
                new HashMap<String, String>() {{
                    put("param_1", "test");
                }}
            },
            {
                "param_1=test1&param_2=test2",
                new HashMap<String, String>() {{
                    put("param_1", "test1");
                    put("param_2", "test2");
                }}
            },
            {
                "boolean",
                new HashMap<String, String>() {{
                    put("boolean", "");
                }}
            },
            {
                "boolean_1&boolean_2",
                new HashMap<String, String>() {{
                    put("boolean_1", "");
                    put("boolean_2", "");
                }}
            },
            {
                "boolean&param_2=test2",
                new HashMap<String, String>() {{
                    put("boolean", "");
                    put("param_2", "test2");
                }}
            },

        };

        return result;

    }

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "test1",
        "test2",
        "test3",
        "ciao",
        "random string",
        ""
    })
    void getPath_test(String path) {

        when(uriMock.getPath())
            .thenReturn(path);

        Request request = new Request(HttpMethod.GET, "", headersMock, uriMock);

        assertEquals(path, request.getPath());

    }

    @ParameterizedTest
    @MethodSource("parseQueryStringTestParameters")
    void parseQueryStringTest(String queryString, Map<String, String> expected) {

        when(uriMock.getQuery())
            .thenReturn(queryString);

        Request request = new Request(
            HttpMethod.GET,
            "",
            headersMock,
            uriMock
        );

        assertEquals(expected.size(), request.getQueryMap().size());

        for (Map.Entry<String, String> expectedEntry : expected.entrySet()) {

            String actualValue = request.getQueryParam(expectedEntry.getKey());
            String expectedValue = expectedEntry.getValue();

            assertEquals(expectedValue, actualValue);


        }


    }

}