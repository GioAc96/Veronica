package org.gioac96.veronica.routing;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.gioac96.veronica.http.HttpMethod;
import org.gioac96.veronica.http.Request;

public class RequestParser {

    public Request parseExchange(HttpExchange httpExchange) throws IOException {

        HttpMethod httpMethod = HttpMethod.fromName(httpExchange.getRequestMethod());
        String body = new String(httpExchange.getRequestBody().readAllBytes());

        return Request.builder()
            .httpMethod(httpMethod)
            .uri(httpExchange.getRequestURI())
            .headers(httpExchange.getRequestHeaders())
            .body(body)
            .build();

    }

}
