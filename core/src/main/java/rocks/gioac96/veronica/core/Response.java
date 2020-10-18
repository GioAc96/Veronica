package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.Headers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;

/**
 * Http response.
 */
public class Response {

    @Getter
    protected final HttpStatus httpStatus;
    @Getter
    private final Headers headers;
    @Getter
    private final Set<SetCookieHeader> cookies;
    private byte[] bodyBytes;
    @Getter
    private String body;

    protected Response(ResponseBuilder b) {

        this.httpStatus = b.httpStatus;
        this.body = b.body;
        this.bodyBytes = b.bodyBytes;
        this.headers = b.headers;
        this.cookies = b.cookies;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static ResponseBuilder builder() {

        return new ResponseBuilder();

    }

    public byte[] getBodyBytes() {

        if (bodyBytes == null) {

            if (body == null) {

                return new byte[0];

            } else {

                return body.getBytes();

            }

        } else {

            return bodyBytes;

        }

    }

    /**
     * Checks whether the response is already rendered.
     *
     * @return true iff the response is already rendered
     */
    public boolean isRendered() {

        return body != null;

    }

    /**
     * Writes the body of the response if the response is not already rendered.
     *
     * @param body body of the response
     * @return true iff the response was not already rendered and the body was successfully written
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean writeBody(@NonNull String body) {

        this.body = body;
        return writeBody(body.getBytes());

    }

    /**
     * Writes the body of the response if the response is not already rendered.
     *
     * @param body body of the response
     * @return true iff the response was not already rendered and the body was successfully written
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean writeBody(@NonNull byte[] body) {

        if (isRendered()) {

            return false;

        } else {

            this.bodyBytes = body;

            return true;

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class ResponseBuilder extends ConfigurableProvider<Response> {

        private Set<SetCookieHeader> cookies;
        private HttpStatus httpStatus = HttpStatus.OK;
        private byte[] bodyBytes;
        private String body;
        private Headers headers = new Headers();

        public ResponseBuilder httpStatus(@NonNull HttpStatus httpStatus) {

            this.httpStatus = httpStatus;

            return this;

        }

        public ResponseBuilder body(@NonNull String body) {

            this.body = body;
            return body(body.getBytes());

        }

        public ResponseBuilder body(@NonNull byte[] body) {

            this.bodyBytes = body;
            return this;

        }

        public ResponseBuilder emptyBody() {

            return body("");

        }

        public ResponseBuilder headers(@NonNull Headers headers) {

            this.headers = headers;

            return this;

        }

        public ResponseBuilder requestBasicAuth(@NonNull String realm) {

            return header("WWW-Authenticate", "Basic realm=\"" + realm + '\"');

        }

        public ResponseBuilder requestBasicAuth() {

            return header("WWW-Authenticate", "Basic");

        }

        public ResponseBuilder header(@NonNull String key, @NonNull String value) {

            if (this.headers.containsKey(key)) {

                this.headers.get(key).add(value);

            } else {

                this.headers.put(key, new ArrayList<>() {{
                    add(value);
                }});

            }

            return this;

        }

        @SuppressWarnings({"checkstyle:RightCurly", "checkstyle:Indentation"})
        public ResponseBuilder header(@NonNull String key, @NonNull Collection<String> values) {

            if (this.headers.containsKey(key)) {

                this.headers.get(key).addAll(values);

            } else {

                this.headers.put(key, new ArrayList<String>() {{
                    addAll(values);
                }});

            }

            return this;

        }

        private void initCookies() {

            if (cookies == null) {

                cookies = new HashSet<>();

            }

        }

        public ResponseBuilder cookies(@NonNull Collection<SetCookieHeader> cookies) {

            initCookies();

            this.cookies.addAll(cookies);

            return this;

        }

        public ResponseBuilder cookie(@NonNull SetCookieHeader cookie) {

            initCookies();

            this.cookies.add(cookie);

            return this;

        }

        @Override
        protected boolean isValid() {

            return httpStatus != null;

        }

        @Override
        protected Response instantiate() {

            return new Response(this);

        }

    }

}
