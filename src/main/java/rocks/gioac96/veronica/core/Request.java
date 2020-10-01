package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.Headers;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Http Request.
 */
@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Request {

    @Getter
    @Setter
    private static Charset COOKIE_VALUE_CHARSET = StandardCharsets.UTF_8;

    @Getter
    @NonNull
    protected final HttpMethod httpMethod;

    @Getter
    @NonNull
    protected final String body;

    @Getter
    @NonNull
    protected final Headers headers;

    @Getter
    @NonNull
    protected final URI uri;

    @Getter
    protected final boolean secure;

    @Getter(lazy = true)
    private final Map<String, String> queryMap = lazyLoadQueryMap();

    @Getter(lazy = true)
    private final Map<String, String> cookie = lazyLoadCookie();

    @Getter(lazy = true)
    private final Map<String, String> variablePathParts = new HashMap<>();

    /**
     * Gets a query parameter's value.
     *
     * @param paramName name of the parameter to get the value of
     * @return the value of the query parameter if specified, null otherwise
     */
    public String getQueryParam(String paramName) {

        return getQueryMap().getOrDefault(paramName, null);

    }

    /**
     * Gets the request path.
     *
     * @return the request path
     */
    public String getPath() {

        return uri.getPath();

    }

    private Map<String, String> lazyLoadQueryMap() {

        Map<String, String> queryMap = new HashMap<>();

        //noinspection ConstantConditions
        String query = uri.getQuery();

        if (query == null || query.length() == 0) {

            return queryMap;

        }

        String[] queryParts = query.split("&");

        for (String queryPart : queryParts) {

            String[] queryParamParts = queryPart.split("=");

            if (queryParamParts.length > 1) {

                queryMap.put(queryParamParts[0], queryParamParts[1]);

            } else {

                queryMap.put(queryParamParts[0], "");

            }

        }

        return queryMap;

    }

    private Map<String, String> lazyLoadCookie() {

        Map<String, String> cookieMap = new HashMap<>();

        //noinspection ConstantConditions
        String cookieHeader = headers.getFirst("cookie");

        if (cookieHeader == null || cookieHeader.length() == 0) {

            return cookieMap;

        }

        String[] cookiePairs = cookieHeader.split(";\\s*");

        for (String cookiePair : cookiePairs) {

            String[] pairParts = cookiePair.split("=");

            if (pairParts.length == 2) {

                cookieMap.put(pairParts[0], URLDecoder.decode(pairParts[1], COOKIE_VALUE_CHARSET));

            }

        }

        return cookieMap;

    }

}
