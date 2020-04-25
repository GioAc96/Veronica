package rocks.gioac96.veronica.routing.pipeline.stages;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.http.Response;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterPayload<S extends Response> {

    @Getter
    private final S response;

    private final boolean shouldContinue;

    public boolean shouldContinue() {

        return shouldContinue;

    }

    public static <S extends Response> FilterPayload<S> ok() {

        return new FilterPayload<>(null, true);

    }

    public static <S extends Response> FilterPayload<S> fail(@NonNull S response) {

        return new FilterPayload<>(response, false);

    }

}
