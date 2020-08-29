package rocks.gioac96.veronica.routing.pipeline;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.pipeline.stages.AsynchronousPostProcessor;
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
public class Pipeline<Q extends Request, S extends Response> {

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PreFilter<Q, S>> preFilters;

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PostFilter<Q, S>> postFilters;

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PostProcessor<Q, S>> postProcessors;

    @Getter
    @Setter
    private ResponseRenderer<S> responseRenderer;

    protected Pipeline(PipelineBuilder<Q, S, ?, ?> b) {
        this.preFilters = b.preFilters;
        this.postFilters = b.postFilters;
        this.postProcessors = b.postProcessors;
        this.responseRenderer = b.responseRenderer;
    }

    public static <Q extends Request, S extends Response> PipelineBuilder<Q, S, ?, ?> builder() {
        return new PipelineBuilderImpl<>();
    }

    private S preRender(Q request, RequestHandler<Q, S> requestHandler) {

        for (PreFilter<Q, S> preFilter : preFilters) {

            FilterPayload<S> filterPayload = preFilter.filter(request);

            if (!filterPayload.shouldContinue()) {
                return filterPayload.getResponse();
            }

        }

        RequestHandlerPayload<S> requestHandlerPayload = requestHandler.handle(request);
        S response = requestHandlerPayload.getResponse();

        if (!requestHandlerPayload.shouldContinue()) {

            return response;

        }

        for (PostFilter<Q, S> postFilter : postFilters) {

            FilterPayload<S> filterPayload = postFilter.filter(request, response);

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
    public S handle(@NonNull Q request, @NonNull RequestHandler<Q, S> requestHandler) {

        S response = preRender(request, requestHandler);

        if (!response.isRendered()) {

            if (responseRenderer == null) {

                throw new UnrenderedResponseException(response);

            } else {

                response.writeBody(responseRenderer.render(response));

            }

        }

        for (PostProcessor<Q, S> postProcessor : postProcessors) {

            if (postProcessor instanceof AsynchronousPostProcessor) {

                Thread postProcessorThread = new Thread(() -> postProcessor.process(request, response));
                postProcessorThread.setPriority(((AsynchronousPostProcessor<Q, S>) postProcessor).getThreadPriority());

                postProcessorThread.start();

            } else {

                postProcessor.process(request, response);

            }

        }

        return response;

    }


    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "UnusedReturnValue"})
    public abstract static class PipelineBuilder<
        Q extends Request,
        S extends Response,
        C extends Pipeline<Q, S>,
        B extends PipelineBuilder<Q, S, C, B>
        > {

        @NonNull
        private final PrioritySet<PreFilter<Q, S>> preFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostFilter<Q, S>> postFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostProcessor<Q, S>> postProcessors = new PrioritySet<>();

        private ResponseRenderer<S> responseRenderer;

        @SuppressWarnings("unused")
        public B preFilter(PreFilter<Q, S> preFilter) {

            this.preFilters.add(preFilter);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilter(PreFilter<Q, S> preFilter, Integer priority) {

            this.preFilters.add(preFilter, priority);
            return self();

        }

        public B preFilters(PrioritySet<PreFilter<Q, S>> preFilters) {

            this.preFilters.addAll(preFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilters(Collection<PreFilter<Q, S>> preFilters) {

            this.preFilters.addAll(preFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilter(PostFilter<Q, S> postFilter) {

            this.postFilters.add(postFilter);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilter(PostFilter<Q, S> postFilter, Integer priority) {

            this.postFilters.add(postFilter, priority);
            return self();

        }

        public B postFilters(PrioritySet<PostFilter<Q, S>> postFilters) {

            this.postFilters.addAll(postFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postFilters(Collection<PostFilter<Q, S>> postFilters) {

            this.postFilters.addAll(postFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessor(PostProcessor<Q, S> postProcessor) {

            this.postProcessors.add(postProcessor);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessor(PostProcessor<Q, S> postProcessor, Integer priority) {

            this.postProcessors.add(postProcessor, priority);
            return self();

        }

        public B postProcessors(PrioritySet<PostProcessor<Q, S>> postProcessors) {

            this.postProcessors.addAll(postProcessors);
            return self();

        }

        @SuppressWarnings("unused")
        public B postProcessors(Collection<PostProcessor<Q, S>> postProcessors) {

            this.postProcessors.addAll(postProcessors);
            return self();

        }

        public B responseRenderer(ResponseRenderer<S> responseRenderer) {

            this.responseRenderer = responseRenderer;
            return self();

        }

        @SuppressWarnings("unused")
        public B pipeline(Pipeline<Q, S> pipeline) {

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
    private static final class PipelineBuilderImpl<
        Q extends Request,
        S extends Response
        > extends PipelineBuilder<Q, S, Pipeline<Q, S>, PipelineBuilderImpl<Q, S>> {

        protected Pipeline.PipelineBuilderImpl<Q, S> self() {

            return this;

        }

        public Pipeline<Q, S> build() {

            return new Pipeline<>(this);

        }

    }
}
