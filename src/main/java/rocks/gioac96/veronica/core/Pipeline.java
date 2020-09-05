package rocks.gioac96.veronica.core;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.DeclaresPriority;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public final class Pipeline {

    @NonNull
    @Getter
    @Setter
    private PrioritySet<PreFilter> preFilters;

    @NonNull
    @Getter
    @Setter
    private PrioritySet<PostFilter> postFilters;

    @NonNull
    @Getter
    @Setter
    private PrioritySet<PostProcessor> postProcessors;

    @Getter
    @Setter
    private ResponseRenderer responseRenderer;

    @Setter
    protected ThreadPoolExecutor threadPool;

    protected Pipeline(PipelineBuilder b) {

        this.preFilters = b.preFilters;
        this.postFilters = b.postFilters;
        this.postProcessors = b.postProcessors;
        this.responseRenderer = b.responseRenderer;

    }

    public static PipelineBuilder builder() {
        return new PipelineBuilder();
    }

    private Response preRender(Request request, RequestHandler requestHandler) {

        for (PreFilter preFilter : preFilters) {

            preFilter.filter(request);

        }

        Response response = requestHandler.handle(request);

        for (PostFilter postFilter : postFilters) {

            postFilter.filter(request, response);

        }

        return response;

    }

    /**
     * Handles a request by passing it through the pipeline.
     *
     * @param request        request to handle
     * @param requestHandler request handler that performs the requested action
     * @return the generated response
     */
    public Response handle(@NonNull Request request, @NonNull RequestHandler requestHandler) {

        Response response;

        try {

            response = preRender(request, requestHandler);

        } catch (PipelineBreakException e) {

            response = e.getResponse();

        }

        render(response);

        postRender(request, response);

        return response;

    }

    private void postRender(@NonNull Request request, Response response) {

        for (PostProcessor postProcessor : postProcessors) {

            if (postProcessor instanceof PostProcessor.Asynchronous) {

                Runnable postProcessorTask = () -> postProcessor.process(request, response);

                if (this.threadPool == null) {

                    new Thread(postProcessorTask).start();

                } else {

                    this.threadPool.execute(postProcessorTask);

                }


            } else {

                postProcessor.process(request, response);

            }

        }

    }
    
    private void render(Response response) {

        if (!response.isRendered()) {

            if (responseRenderer == null) {

                response.writeBody("");

            } else {

                String body;

                try {

                    body = responseRenderer.render(response);

                } catch (Exception e) {

                    response.writeBody("");
                    throw e;

                }

                response.writeBody(body);

            }

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "UnusedReturnValue"})
    public static class PipelineBuilder extends Builder<Pipeline> {

        @NonNull
        private final PrioritySet<PreFilter> preFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostFilter> postFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();

        private ResponseRenderer responseRenderer;

        public PipelineBuilder preFilter(PreFilter preFilter) {

            this.preFilters.add(preFilter);
            return this;

        }

        public PipelineBuilder preFilter(PreFilter preFilter, Integer priority) {

            this.preFilters.add(preFilter, priority);
            return this;

        }

        public PipelineBuilder preFilter(Provider<PreFilter> preFilterProvider) {

            if (preFilterProvider instanceof DeclaresPriority) {

                this.preFilters.add(preFilterProvider.provide(), ((DeclaresPriority)preFilterProvider).priority());

            } else {

                this.preFilters.add(preFilterProvider.provide());

            }
            return this;

        }

        public PipelineBuilder postFilter(PostFilter postFilter) {

            this.postFilters.add(postFilter);
            return this;

        }

        public PipelineBuilder postFilter(PostFilter postFilter, Integer priority) {

            this.postFilters.add(postFilter, priority);
            return this;

        }

        public PipelineBuilder postFilters(Provider<PostFilter> postFilterProvider) {

            if (postFilterProvider instanceof DeclaresPriority) {
                
                return this.postFilter(
                    postFilterProvider.provide(),
                    ((DeclaresPriority) postFilterProvider
                ).priority());
                
            } else {
                
                return this.postFilter(postFilterProvider.provide());
                
            }
            
        }

        public PipelineBuilder postProcessor(PostProcessor postProcessor) {

            this.postProcessors.add(postProcessor);
            return this;

        }

        public PipelineBuilder postProcessor(PostProcessor postProcessor, Integer priority) {

            this.postProcessors.add(postProcessor, priority);
            return this;

        }

        public PipelineBuilder postProcessor(Provider<PostProcessor> postProcessorProvider) {

            if (postProcessorProvider instanceof DeclaresPriority) {
                
                return postProcessor(
                    postProcessorProvider.provide(),
                    ((DeclaresPriority) postProcessorProvider).priority()
                );
                
            } else {
                
                return postProcessor(postProcessorProvider.provide());
                
            }

        }

        public PipelineBuilder responseRenderer(ResponseRenderer responseRenderer) {

            this.responseRenderer = responseRenderer;
            return this;

        }

        public PipelineBuilder combinePipeline(Pipeline pipeline) {

            preFilters.addAll(pipeline.preFilters);
            postFilters.addAll(pipeline.postFilters);
            postProcessors.addAll(pipeline.postProcessors);

            if (pipeline.responseRenderer != null) {

                responseRenderer(pipeline.responseRenderer);

            }

            return this;

        }

        @Override
        protected Pipeline instantiate() {

            return new Pipeline(this);

        }

    }

}
