package rocks.gioac96.veronica.pipeline;

import rocks.gioac96.veronica.Request;
import rocks.gioac96.veronica.Response;

public interface PipelineStage<D> {

    Response filter(Request request, Response.ResponseBuilder responseBuilder, D data);

}
