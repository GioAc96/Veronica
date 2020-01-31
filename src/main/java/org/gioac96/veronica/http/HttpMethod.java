package org.gioac96.veronica.http;

public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    HEAD;

    public static HttpMethod fromName(String name) {

        return HttpMethod.valueOf(name.toUpperCase());

    }

}
