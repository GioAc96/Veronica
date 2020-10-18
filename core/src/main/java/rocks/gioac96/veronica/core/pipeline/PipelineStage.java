package rocks.gioac96.veronica.core.pipeline;

import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

public interface PipelineStage<D> {

    Response filter(Request request, Response.ResponseBuilder responseBuilder, D data);

}
