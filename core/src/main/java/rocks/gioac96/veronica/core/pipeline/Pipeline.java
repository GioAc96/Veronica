package rocks.gioac96.veronica.core.pipeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.common.CommonExecutorServices;
import rocks.gioac96.veronica.core.concurrency.PriorityExecutorService;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.util.HasPriority;
import rocks.gioac96.veronica.core.util.PriorityListBuilder;

/**
 * Request pipeline.
 */
public class Pipeline<D> implements RequestHandler {

    private final List<PipelineStage<D>> stages;
    private final List<PostProcessor<D>> postProcessors;
    private final PipelineDataFactory<D> dataFactory;
    private final PipelineResponseBuilderFactory responseBuilderFactory;
    private final PriorityExecutorService postProcessorsExecutor;

    public Pipeline(PipelineBuilder<D> b) {

        this.stages = b.generateStagesList();
        this.postProcessors = b.generatePostProcessorsList();
        this.dataFactory = b.dataFactory;
        this.responseBuilderFactory = b.responseBuilderFactory;
        this.postProcessorsExecutor = b.postProcessorsExecutor;

    }

    public static <D> PipelineBuilder<D> builder() {

        return new PipelineBuilder<>();

    }

    @Override
    public Response handle(Request request) {

        D data = dataFactory.initData(request);
        Response.ResponseBuilder responseBuilder = responseBuilderFactory.initResponseBuilder();

        Response response = null;

        for (PipelineStage<D> stage : stages) {

            response = stage.filter(request, responseBuilder, data);

            if (response != null) {

                break;

            }

        }

        final Response finalResponse = (response == null ? responseBuilder.provide() : response);

        for (PostProcessor<D> postProcessor : postProcessors) {

            postProcessorsExecutor.execute(
                () -> postProcessor.process(request, finalResponse, data),
                postProcessor.getPriority()
            );

        }

        return finalResponse;

    }

    public static class PipelineBuilder<D> extends ConfigurableProvider<Pipeline<D>> {

        private final PriorityListBuilder<PipelineStage<D>> stagesListBuilder = new PriorityListBuilder<>();
        private final PriorityListBuilder<PostProcessor<D>> postProcessorsListBuilder = new PriorityListBuilder<>();
        protected PipelineDataFactory<D> dataFactory = request -> null;
        protected PipelineResponseBuilderFactory responseBuilderFactory = Response::builder;
        protected PriorityExecutorService postProcessorsExecutor = CommonExecutorServices.defaultPriorityExecutorService();

        public PipelineBuilder<D> stage(@NonNull PipelineStage<D> stage, int priority) {

            stagesListBuilder.put(stage, priority);

            return this;

        }

        public PipelineBuilder<D> stage(@NonNull PipelineStage<D> stage) {

            return stage(stage, Integer.MAX_VALUE);

        }

        public PipelineBuilder<D> stage(@NonNull Provider<PipelineStage<D>> stageProvider) {

            if (stageProvider instanceof HasPriority) {

                return stage(stageProvider.provide(), ((HasPriority) stageProvider).getPriority());

            } else {

                return stage(stageProvider.provide());

            }

        }

        public PipelineBuilder<D> postProcessor(@NonNull PostProcessor<D> postProcessor) {

            return postProcessor(postProcessor, Integer.MAX_VALUE);

        }

        public PipelineBuilder<D> postProcessor(@NonNull PostProcessor<D> postProcessor, int priority) {


            postProcessorsListBuilder.put(postProcessor, priority);

            return this;

        }

        public PipelineBuilder<D> postProcessor(@NonNull Provider<PostProcessor<D>> postProcessorProvider) {

            if (postProcessorProvider instanceof HasPriority) {

                return postProcessor(postProcessorProvider.provide(), ((HasPriority) postProcessorProvider).getPriority());

            } else {

                return postProcessor(postProcessorProvider.provide());

            }

        }

        public PipelineBuilder<D> dataFactory(@NonNull PipelineDataFactory<D> dataFactory) {

            this.dataFactory = dataFactory;
            return this;

        }

        public PipelineBuilder<D> dataFactory(@NonNull Provider<PipelineDataFactory<D>> dataFactoryProvider) {

            return dataFactory(dataFactoryProvider.provide());

        }

        public PipelineBuilder<D> postProcessorsExecutor(@NonNull PriorityExecutorService postProcessorsExecutor) {

            this.postProcessorsExecutor = postProcessorsExecutor;
            return this;

        }

        public PipelineBuilder<D> postProcessorsExecutor(@NonNull Provider<PriorityExecutorService> postProcessorsExecutorProvider) {

            return postProcessorsExecutor(postProcessorsExecutorProvider.provide());

        }

        public PipelineBuilder<D> responseBuilderFactory(@NonNull PipelineResponseBuilderFactory responseBuilderFactory) {

            this.responseBuilderFactory = responseBuilderFactory;
            return this;

        }

        public PipelineBuilder<D> responseBuilderFactory(@NonNull Provider<PipelineResponseBuilderFactory> responseBuilderFactoryProvider) {

            return responseBuilderFactory(responseBuilderFactoryProvider.provide());

        }

        @Override
        protected boolean isValid() {

            return dataFactory != null
                && responseBuilderFactory != null
                && postProcessorsExecutor != null;

        }

        @Override
        protected Pipeline<D> instantiate() {

            return new Pipeline<>(this);

        }

        public List<PipelineStage<D>> generateStagesList() {

            return stagesListBuilder.toArrayList();

        }

        public List<PostProcessor<D>> generatePostProcessorsList() {

            return postProcessorsListBuilder.toArrayList();

        }

    }

}
