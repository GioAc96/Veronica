package org.gioac96.veronica.http;

import com.sun.net.httpserver.Headers;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Http Request.
 */
public class Request {

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

    @Getter(lazy = true)
    private final Map<String, String> queryMap = lazyLoadQueryMap();

    @Getter(lazy = true)
    private final List<HttpCookie> cookie = lazyLoadCookie();

    @Builder
    public Request(
        @NonNull HttpMethod httpMethod,
        @NonNull String body,
        @NonNull Headers headers,
        @NonNull URI uri
    ) {

        this.httpMethod = httpMethod;
        this.body = body;
        this.headers = headers;
        this.uri = uri;

    }

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

        if (uri.getQuery() == null || uri.getQuery().length() == 0) {

            return queryMap;

        }

        String[] queryParts = uri.getQuery().split("&");

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

    private List<HttpCookie> lazyLoadCookie() {

        return HttpCookie.parse(headers.getFirst("cookie"));

    }

}
