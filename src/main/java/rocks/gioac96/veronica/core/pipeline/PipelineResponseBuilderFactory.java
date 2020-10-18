package rocks.gioac96.veronica.core.pipeline;

import rocks.gioac96.veronica.core.Response;

public interface PipelineResponseBuilderFactory {

    Response.ResponseBuilder initResponseBuilder();

}
