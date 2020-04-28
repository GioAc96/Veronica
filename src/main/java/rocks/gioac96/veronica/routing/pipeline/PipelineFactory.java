package rocks.gioac96.veronica.routing.pipeline;

import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.PostFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.PostProcessor;
import rocks.gioac96.veronica.routing.pipeline.stages.PreFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.ResponseRenderer;

/**
 * Pipeline factory.
 *
 * @param <Q> Request type
 * @param <S> Response type
 */
public abstract class PipelineFactory<
    Q extends Request,
    S extends Response
    > extends Pipeline.PipelineBuilder<
    Q,
    S,
    Pipeline<Q, S>,
    PipelineFactory<Q, S>
    > implements Factory<Pipeline<Q, S>> {

    protected PipelineFactory<Q, S> preFilter(Factory<PreFilter<Q, S>> preFilterFactory) throws CreationException {

        return super.preFilter(preFilterFactory.build(), preFilterFactory.priority());

    }

    protected PipelineFactory<Q, S> postFilter(Factory<PostFilter<Q, S>> postFilterFactory) throws CreationException {

        return super.postFilter(postFilterFactory.build(), postFilterFactory.priority());

    }

    protected PipelineFactory<Q, S> postProcessor(Factory<PostProcessor<Q, S>> postProcessorFactory)
        throws CreationException {

        return super.postProcessor(postProcessorFactory.build(), postProcessorFactory.priority());

    }

    protected PipelineFactory<Q, S> responseRenderer(Factory<ResponseRenderer<S>> responseRendererFactory)
        throws CreationException {

        return super.responseRenderer(responseRendererFactory.build());

    }

    @Override
    protected PipelineFactory<Q, S> self() {

        return this;

    }

    @Override
    public Pipeline<Q, S> build() {

        configure();

        return new Pipeline<>(this);

    }

}
