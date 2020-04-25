package rocks.gioac96.veronica.http;

/**
 * Http methods.
 */
@SuppressWarnings("unused")
public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    HEAD;

    /**
     * Returns an @{link HttpMethod} by its name.
     *
     * @param name name of the HttpMethod
     * @return an @{link HttpMethod} corresponding to the specified method name
     */
    public static HttpMethod fromName(String name) {

        return HttpMethod.valueOf(name.toUpperCase());

    }

}
