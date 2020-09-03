package rocks.gioac96.veronica.routing.pipeline;

import rocks.gioac96.veronica.factories.ConfigurableFactory;
import rocks.gioac96.veronica.factories.CreationException;
import rocks.gioac96.veronica.factories.Factory;
import rocks.gioac96.veronica.factories.PriorityFactory;
import rocks.gioac96.veronica.routing.pipeline.stages.PostFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.PostProcessor;
import rocks.gioac96.veronica.routing.pipeline.stages.PreFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.ResponseRenderer;

/**
 * Pipeline factory.
 *
 */
public abstract class PipelineFactory
    extends Pipeline.PipelineBuilder<Pipeline, PipelineFactory>
    implements ConfigurableFactory<Pipeline> {

    protected PipelineFactory preFilter(Factory<PreFilter> preFilterFactory) throws CreationException {

        return super.preFilter(preFilterFactory.build());

    }

    protected PipelineFactory preFilter(PriorityFactory<PreFilter> preFilterFactory)
        throws CreationException {

        return super.preFilter(preFilterFactory.build(), preFilterFactory.priority());

    }

    protected PipelineFactory postFilter(Factory<PostFilter> postFilterFactory) throws CreationException {

        return super.postFilter(postFilterFactory.build());

    }

    protected PipelineFactory postFilter(PriorityFactory<PostFilter> postFilterFactory)
        throws CreationException {

        return super.postFilter(postFilterFactory.build(), postFilterFactory.priority());

    }

    protected PipelineFactory postProcessor(Factory<PostProcessor> postProcessorFactory)
        throws CreationException {

        return super.postProcessor(postProcessorFactory.build());

    }

    protected PipelineFactory postProcessor(PriorityFactory<PostProcessor> postProcessorFactory)
        throws CreationException {

        return super.postProcessor(postProcessorFactory.build(), postProcessorFactory.priority());

    }

    protected PipelineFactory responseRenderer(Factory<ResponseRenderer> responseRendererFactory)
        throws CreationException {

        return super.responseRenderer(responseRendererFactory.build());

    }

    @Override
    protected PipelineFactory self() {

        return this;

    }

    @Override
    public Pipeline build() {

        configure();

        return new Pipeline(this);

    }

}
