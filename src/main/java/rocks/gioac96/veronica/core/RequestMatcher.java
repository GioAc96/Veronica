package rocks.gioac96.veronica.core;

/**
 * Request matching rule.
 */
public interface RequestMatcher {

    boolean matches(Request request);

}
