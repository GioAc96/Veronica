package rocks.gioac96.veronica.routing.pipeline;

import java.util.Collection;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.FilterPayload;
import rocks.gioac96.veronica.routing.pipeline.stages.PostFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.PostProcessor;
import rocks.gioac96.veronica.routing.pipeline.stages.PreFilter;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandler;
import rocks.gioac96.veronica.routing.pipeline.stages.RequestHandlerPayload;
import rocks.gioac96.veronica.routing.pipeline.stages.ResponseRenderer;
import rocks.gioac96.veronica.routing.pipeline.stages.UnrenderedResponseException;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Pipeline {

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PreFilter> preFilters;

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PostFilter> postFilters;

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PostProcessor> postProcessors;

    @Getter
    @Setter
    private ResponseRenderer responseRenderer;

    @Setter
    protected ThreadPoolExecutor threadPool;

    protected Pipeline(PipelineBuilder<?, ?> b) {

        this.preFilters = b.preFilters;
        this.postFilters = b.postFilters;
        this.postProcessors = b.postProcessors;
        this.responseRenderer = b.responseRenderer;

    }

    public static  PipelineBuilder<?, ?> builder() {
        return new PipelineBuilderImpl();
    }

    private Response preRender(Request request, RequestHandler requestHandler) {

        for (PreFilter preFilter : preFilters) {

            FilterPayload filterPayload = preFilter.filter(request);

            if (!filterPayload.shouldContinue()) {
                return filterPayload.getResponse();
            }

        }

        RequestHandlerPayload requestHandlerPayload = requestHandler.handle(request);
        Response response = requestHandlerPayload.getResponse();

        if (!requestHandlerPayload.shouldContinue()) {

            return response;

        }

        for (PostFilter postFilter : postFilters) {

            FilterPayload filterPayload = postFilter.filter(request, response);

            if (!filterPayload.shouldContinue()) {

                return filterPayload.getResponse();

            }

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

        Response response = preRender(request, requestHandler);

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

                throw new UnrenderedResponseException(response);

            } else {

                response.writeBody(responseRenderer.render(response));

            }

        }

    }

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "UnusedReturnValue"})
    public abstract static class PipelineBuilder<
        C extends Pipeline,
        B extends PipelineBuilder<C, B>
        > {

        @NonNull
        private final PrioritySet<PreFilter> preFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostFilter> postFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();

        private ResponseRenderer responseRenderer;

        @SuppressWarnings("unused")
        public B preFilter(PreFilter preFilter) {

            this.preFilters.add(preFilter);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilter(PreFilter preFilter, Integer priority) {

            this.preFilters.add(preFilter, priority);
            return self();

        }

        public B preFilters(PrioritySet<PreFilter> preFilters) {

            this.preFilters.addAll(preFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilters(Collection<PreFilter> preFilters) {

            this.preFilters.addAll(preFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilter(PostFilter postFilter) {

            this.postFilters.add(postFilter);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilter(PostFilter postFilter, Integer priority) {

            this.postFilters.add(postFilter, priority);
            return self();

        }

        public B postFilters(PrioritySet<PostFilter> postFilters) {

            this.postFilters.addAll(postFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilters(Collection<PostFilter> postFilters) {

            this.postFilters.addAll(postFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessor(PostProcessor postProcessor) {

            this.postProcessors.add(postProcessor);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessor(PostProcessor postProcessor, Integer priority) {

            this.postProcessors.add(postProcessor, priority);
            return self();

        }

        public B postProcessors(PrioritySet<PostProcessor> postProcessors) {

            this.postProcessors.addAll(postProcessors);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessors(Collection<PostProcessor> postProcessors) {

            this.postProcessors.addAll(postProcessors);
            return self();

        }

        public B responseRenderer(ResponseRenderer responseRenderer) {

            this.responseRenderer = responseRenderer;
            return self();

        }

        @SuppressWarnings("unused")
        public B pipeline(Pipeline pipeline) {

            preFilters(pipeline.getPreFilters());
            postFilters(pipeline.getPostFilters());
            postProcessors(pipeline.getPostProcessors());

            if (pipeline.getResponseRenderer() != null) {

                responseRenderer(pipeline.getResponseRenderer());

            }

            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class PipelineBuilderImpl extends PipelineBuilder<Pipeline, PipelineBuilderImpl> {

        protected Pipeline.PipelineBuilderImpl self() {

            return this;

        }

        public Pipeline build() {

            return new Pipeline(this);

        }

    }
}
