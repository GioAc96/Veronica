package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHandlerPayload<S extends Response> {

    @Getter
    @NonNull
    private final S response;

    private final boolean shouldContinue;

    public boolean shouldContinue() {

        return shouldContinue;

    }

    public static <S extends Response> RequestHandlerPayload<S> ok(S response) {

        return new RequestHandlerPayload<>(response, true);

    }

    public static <S extends Response> RequestHandlerPayload<S> fail(S response) {

        return new RequestHandlerPayload<>(response, false);

    }

}
