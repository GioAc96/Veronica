package rocks.gioac96.veronica.http;

/**
 * Http methods.
 */
public enum HttpMethod {

    GET,
    POST,
    @SuppressWarnings("unused") PUT,
    @SuppressWarnings("unused") DELETE,
    @SuppressWarnings("unused") HEAD;

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
