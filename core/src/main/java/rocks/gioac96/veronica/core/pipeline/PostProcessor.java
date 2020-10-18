package rocks.gioac96.veronica.core.pipeline;

import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.util.HasPriority;

public interface PostProcessor<D> extends HasPriority {

    void process(
        Request request,
        Response response,
        D pipelineData
    );

    @Override
    default int getPriority() {

        return Integer.MAX_VALUE;

    }
}
