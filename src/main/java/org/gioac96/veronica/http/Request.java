package org.gioac96.veronica.http;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Request {

    @Getter
    protected final HttpMethod httpMethod;

    @Getter
    protected final String body;

    public Request(HttpMethod httpMethod, String body) {

        this.httpMethod = httpMethod;
        this.body = body;

    }


}
