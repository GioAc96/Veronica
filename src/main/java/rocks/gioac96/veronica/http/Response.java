package rocks.gioac96.veronica.http;

import com.sun.net.httpserver.Headers;
import java.net.HttpCookie;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.routing.pipeline.ResponseRenderer;
import rocks.gioac96.veronica.routing.pipeline.ResponseRenderingException;
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
    private String body;

    @Getter
    @NonNull
    @Setter
    private Headers headers;

    @Getter
    @Setter
    private ArraySet<HttpCookie> cookies;

    protected Response(ResponseBuilder<?, ?> builder) {

        this.httpStatus = builder.httpStatus;
        this.body = builder.body;
        this.headers = builder.headers;
        this.cookies = builder.cookies;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static ResponseBuilder<?, ?> builder() {

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
     * Renders the response with the specified renderer, if the response is not already rendered.
     *
     * @param responseRenderer renderer to use to rendered the response
     * @return true iff the response was not already rendered
     * @throws ResponseRenderingException on rendering failure
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean render(@NonNull ResponseRenderer responseRenderer) throws ResponseRenderingException {

        /*
         * Not calling the setBody method to avoid rendering the response with the responseRenderer
         * if it is already rendered.
         */

        if (isRendered()) {

            return false;

        } else {

            this.body = responseRenderer.render(this);

            return true;

        }

    }

    /**
     * Sets the body of the response if the response is not already rendered.
     *
     * @param body body of the response
     * @return true if the response was not already rendered and the body was successfully set
     */
    public boolean setBody(@NonNull String body) {

        if (isRendered()) {

            return false;

        } else {

            this.body = body;

            return true;

        }

    }

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class ResponseBuilder<C extends Response, B extends ResponseBuilder<C, B>> {

        private HttpStatus httpStatus = HttpStatus.OK;
        private String body = null;
        private Headers headers = new Headers();
        private ArraySet<HttpCookie> cookies = new ArraySet<>();

        public B httpStatus(@NonNull HttpStatus httpStatus) {

            this.httpStatus = httpStatus;

            return self();

        }

        public B body(String body) {

            this.body = body;

            return self();

        }

        public B headers(@NonNull Headers headers) {

            this.headers = headers;

            return self();

        }

        public B cookies(Collection<HttpCookie> cookies) {

            this.cookies.addAll(cookies);

            return self();

        }

        public B cookie(HttpCookie cookie) {

            this.cookies.add(cookie);

            return self();

        }

        protected abstract B self();

        public abstract C build();

        public String toString() {

            return
                "Response.ResponseBuilder(httpStatus=" + this.httpStatus
                    + ", body=" + this.body
                    + ", headers=" + this.headers
                    + ", cookies=" + this.cookies
                    + ")";

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ResponseBuilderImpl extends ResponseBuilder<Response, ResponseBuilderImpl> {

        protected Response.ResponseBuilderImpl self() {

            return this;

        }

        public Response build() {

            return new Response(this);

        }

    }

}
