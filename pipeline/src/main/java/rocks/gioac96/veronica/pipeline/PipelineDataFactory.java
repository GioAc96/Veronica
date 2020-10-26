package rocks.gioac96.veronica.pipeline;

import rocks.gioac96.veronica.core.Request;

public interface PipelineDataFactory<D> {

    D initData(Request request);

}
