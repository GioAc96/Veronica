package rocks.gioac96.veronica.core;

import com.sun.net.httpserver.Headers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;

/**
 * Http response.
 */
@EqualsAndHashCode
public class Response {

    @Getter
    protected final HttpStatus httpStatus;

    @Getter
    private final Headers headers;

    private final byte[] bodyBytes;

    @Getter
    private final String bodyString;

    protected Response(ResponseBuilder b) {

        this.httpStatus = b.httpStatus;
        this.bodyString = b.bodyString;
        this.bodyBytes = b.bodyBytes;
        this.headers = b.headers;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static ResponseBuilder builder() {

        return new ResponseBuilder();

    }

    public byte[] getBodyBytes() {

        if (bodyString == null) {

            return bodyBytes;

        } else {

            return bodyString.getBytes();

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class ResponseBuilder extends ConfigurableProvider<Response> {

        private HttpStatus httpStatus = HttpStatus.OK;

        private byte[] bodyBytes;
        private String bodyString;
        private Headers headers = new Headers();

        public ResponseBuilder httpStatus(@NonNull HttpStatus httpStatus) {

            this.httpStatus = httpStatus;

            return this;

        }

        public ResponseBuilder body(@NonNull byte[] bodyBytes) {

            this.bodyBytes = bodyBytes;
            this.bodyString = null;
            return this;

        }

        public ResponseBuilder body(@NonNull String bodyString) {

            this.bodyString = bodyString;
            this.bodyBytes = null;
            return this;

        }

        public ResponseBuilder emptyBody() {

            return body("");

        }

        public ResponseBuilder headers(@NonNull Headers headers) {

            this.headers = headers;

            return this;

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

        public ResponseBuilder cookie(@NonNull SetCookieHeader cookie) {

            return header("Set-Cookie", cookie.toHeaderString());

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
