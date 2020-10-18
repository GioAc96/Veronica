package rocks.gioac96.veronica.pipeline;

import rocks.gioac96.veronica.Request;
import rocks.gioac96.veronica.Response;
import rocks.gioac96.veronica.util.HasPriority;

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
