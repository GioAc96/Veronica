package rocks.gioac96.veronica.http;

import lombok.Getter;

/**
 * Http statuses.
 */
@SuppressWarnings("unused")
public enum HttpStatus {

    @SuppressWarnings("unused") CONTINUE(100, "Continue"),
    @SuppressWarnings("unused") SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    OK(200, "OK"),
    @SuppressWarnings("unused") CREATED(201, "Created"),
    @SuppressWarnings("unused") ACCEPTED(202, "Accepted"),
    @SuppressWarnings("unused") NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    @SuppressWarnings("unused") NO_CONTENT(204, "No Content"),
    @SuppressWarnings("unused") RESET_CONTENT(205, "Reset Content"),
    @SuppressWarnings("unused") PARTIAL_CONTENT(206, "Partial Content"),
    @SuppressWarnings("unused") MULTIPLE_CHOICES(300, "Multiple Choices"),
    @SuppressWarnings("unused") MOVED_PERMANENTLY(301, "Moved Permanently"),
    @SuppressWarnings("unused") FOUND(302, "Found"),
    @SuppressWarnings("unused") SEE_OTHER(303, "See Other"),
    @SuppressWarnings("unused") NOT_MODIFIED(304, "Not Modified"),
    @SuppressWarnings("unused") USE_PROXY(305, "Use Proxy"),
    @SuppressWarnings("unused") TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    @SuppressWarnings("unused") UNAUTHORIZED(401, "Unauthorized"),
    @SuppressWarnings("unused") PAYMENT_REQUIRED(402, "Payment Required"),
    @SuppressWarnings("unused") FORBIDDEN(403, "Forbidden"),
    @SuppressWarnings("unused") NOT_FOUND(404, "Not Found"),
    @SuppressWarnings("unused") METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    @SuppressWarnings("unused") NOT_ACCEPTABLE(406, "Not Acceptable"),
    @SuppressWarnings("unused") PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    @SuppressWarnings("unused") REQUEST_TIMEOUT(408, "Request Timeout"),
    @SuppressWarnings("unused") CONFLICT(409, "Conflict"),
    @SuppressWarnings("unused") GONE(410, "Gone"),
    @SuppressWarnings("unused") LENGTH_REQUIRED(411, "Length Required"),
    @SuppressWarnings("unused") PRECONDITION_FAILED(412, "Precondition Failed"),
    @SuppressWarnings("unused") REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    @SuppressWarnings("unused") REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    @SuppressWarnings("unused") UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    @SuppressWarnings("unused") REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    @SuppressWarnings("unused") EXPECTATION_FAILED(417, "Expectation Failed"),
    @SuppressWarnings("unused") PRECONDITION_REQUIRED(428, "Precondition Required"),
    @SuppressWarnings("unused") TOO_MANY_REQUESTS(429, "Too Many Requests"),
    @SuppressWarnings("unused") REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    @SuppressWarnings("unused") INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    @SuppressWarnings("unused") NOT_IMPLEMENTED(501, "Not Implemented"),
    @SuppressWarnings("unused") BAD_GATEWAY(502, "Bad Gateway"),
    @SuppressWarnings("unused") SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    @SuppressWarnings("unused") GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    @SuppressWarnings("unused") HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    @SuppressWarnings("unused") NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

    @Getter
    private final int code;

    @Getter
    private final String reason;

    HttpStatus(int code, String reason) {

        this.code = code;
        this.reason = reason;

    }


}
