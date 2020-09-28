package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.Headers;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.util.ArraySet;

/**
 * Http response.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Response {

    @Getter
    @Setter
    @NonNull
    protected HttpStatus httpStatus;

    @Getter
    private byte[] body;

    @Getter
    @NonNull
    @Setter
    private Headers headers;

    @Getter
    @Setter
    private ArraySet<SetCookieHeader> cookies;

    protected Response(ResponseBuilder builder) {

        this.httpStatus = builder.httpStatus;
        this.body = builder.body;
        this.headers = builder.headers;
        this.cookies = builder.cookies;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static ResponseBuilder builder() {

        class ResponseBuilderImpl extends ResponseBuilder implements BuildsMultipleInstances {

        }

        return new ResponseBuilderImpl();

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
     * @return true if the response was not already rendered and the body was successfully written
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean writeBody(@NonNull String body) {

        return writeBody(body.getBytes());

    }

    /**
     * Writes the body of the response if the response is not already rendered.
     *
     * @param body body of the response
     * @return true if the response was not already rendered and the body was successfully written
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean writeBody(@NonNull byte[] body) {

        if (isRendered()) {

            return false;

        } else {

            this.body = body;

            return true;

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class ResponseBuilder extends Builder<Response> {

        private final ArraySet<SetCookieHeader> cookies = new ArraySet<>();

        private HttpStatus httpStatus = HttpStatus.OK;

        private byte[] body = null;

        private Headers headers = new Headers();

        public ResponseBuilder httpStatus(@NonNull HttpStatus httpStatus) {

            this.httpStatus = httpStatus;

            return this;

        }

        public ResponseBuilder body(String body) {

            return body(body.getBytes());

        }

        public ResponseBuilder body(byte[] body) {

            this.body = body;

            return this;

        }

        public ResponseBuilder emptyBody() {

            return body("");

        }

        public ResponseBuilder headers(@NonNull Headers headers) {

            this.headers = headers;

            return this;

        }

        public ResponseBuilder requestBasicAuth(String realm) {

            if (realm == null) {

                return header("WWW-Authenticate", "Basic");

            } else {

                return header("WWW-Authenticate", "Basic realm=\"" + realm + '\"');

            }

        }

        public ResponseBuilder requestBasicAuth() {

            return requestBasicAuth(null);

        }

        public ResponseBuilder header(@NonNull String key, @NonNull String value) {

            if (this.headers.containsKey(key)) {

                this.headers.get(key).add(value);

            } else {

                this.headers.put(key, new ArrayList<String>(){{
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

        public ResponseBuilder cookies(@NonNull Collection<SetCookieHeader> cookies) {

            this.cookies.addAll(cookies);

            return this;

        }

        public ResponseBuilder cookie(@NonNull SetCookieHeader cookie) {

            this.cookies.add(cookie);

            return this;

        }

        @Override
        protected Response instantiate() {

            return new Response(this);

        }

    }

}
