package rocks.gioac96.veronica.core.pipeline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.concurrency.PriorityExecutorService;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;
import rocks.gioac96.veronica.util.PriorityEntry;
import rocks.gioac96.veronica.util.PriorityQueueUtils;

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

        private final Map<Integer, List<PipelineStage<D>>> stages = new HashMap<>();
        private final Map<Integer, List<PostProcessor<D>>> postProcessors = new HashMap<>();

        private int stagesSize = 0;
        private int postProcessorsSize = 0;

        protected PipelineDataFactory<D> dataFactory = request -> null;
        protected PipelineResponseBuilderFactory responseBuilderFactory = Response::builder;
        protected PriorityExecutorService postProcessorsExecutor = CommonExecutorServices.defaultPriorityExecutorService();

        public PipelineBuilder<D> stage(@NonNull PipelineStage<D> stage, int priority) {

            List<PipelineStage<D>> samePriorityStagesList = stages.computeIfAbsent(priority, k -> new LinkedList<>());

            samePriorityStagesList.add(stage);

            stagesSize++;

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


            List<PostProcessor<D>> samePriorityPostProcessorsList = postProcessors.computeIfAbsent(priority, k -> new LinkedList<>());

            samePriorityPostProcessorsList.add(postProcessor);

            postProcessorsSize++;

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
                && stagesSize > 0
                && responseBuilderFactory != null
                && postProcessorsExecutor != null;

        }

        @Override
        protected Pipeline<D> instantiate() {

            return new Pipeline<>(this);

        }

        public List<PipelineStage<D>> generateStagesList() {

            List<PipelineStage<D>> pipelineStages = new ArrayList<>(stagesSize);

            stages.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEachOrdered(entry -> {

                pipelineStages.addAll(entry.getValue());

            });

            return pipelineStages;

        }

        public List<PostProcessor<D>> generatePostProcessorsList() {

            List<PostProcessor<D>> pipelinePostProcessors = new ArrayList<>(postProcessorsSize);

            postProcessors.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).forEachOrdered(entry -> {

                pipelinePostProcessors.addAll(entry.getValue());

            });

            return pipelinePostProcessors;

        }

    }

}
