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
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Pipeline<Q extends Request, S extends Response> {

    @Getter
    @Setter
    @NonNull
    private PrioritySet<PreFilter<Q>> preFilters;

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

    private void applyPreFilters(Q request) throws PipelineBreakException {

        for (PreFilter<Q> preFilter : preFilters) {

            preFilter.filter(request);

        }

    }

    private void applyPostFilters(Q request, S response) throws PipelineBreakException {

        for (PostFilter<Q, S> postFilter : postFilters) {

            postFilter.filter(request, response);

        }

    }

    private void applyPostProcessors(Q request, S response) {

        for (PostProcessor<Q, S> postProcessor : postProcessors) {

            postProcessor.process(request, response);

        }

    }

    /**
     * Handles a request by passing through the pipeline.
     *
     * @param request        request to handle
     * @param requestHandler request handler that performs the requested action
     * @return the generated response
     */
    @SuppressWarnings("unchecked")
    public S handle(@NonNull Q request, @NonNull RequestHandler<Q, S> requestHandler) {

        // Pre-render
        S response = preRender(request, requestHandler);

        // Rendering
        if (responseRenderer != null) {

            try {

                response.render(responseRenderer);

            } catch (ResponseRenderingException e) {

                response = (S) e.getResponse();

            }

        }

        // Post-render
        applyPostProcessors(request, response);

        return response;

    }


    @SuppressWarnings("unchecked")
    private S preRender(Q request, RequestHandler<Q, S> requestHandler) {

        try {

            applyPreFilters(request);

            S response = requestHandler.handle(request);

            applyPostFilters(request, response);

            return response;

        } catch (PipelineBreakException e) {

            return (S) e.getResponse();

        }

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
        private final PrioritySet<PreFilter<Q>> preFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostFilter<Q, S>> postFilters = new PrioritySet<>();

        @NonNull
        private final PrioritySet<PostProcessor<Q, S>> postProcessors = new PrioritySet<>();

        private ResponseRenderer<S> responseRenderer;

        @SuppressWarnings("unused")
        public B preFilter(PreFilter<Q> preFilter) {

            this.preFilters.add(preFilter);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilter(PreFilter<Q> preFilter, Integer priority) {

            this.preFilters.add(preFilter, priority);
            return self();

        }

        public B preFilters(PrioritySet<PreFilter<Q>> preFilters) {

            this.preFilters.addAll(preFilters);
            return self();

        }

        @SuppressWarnings("unused")
        public B preFilters(Collection<PreFilter<Q>> preFilters) {

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

        public String toString() {

            return
                "Pipeline.PipelineBuilder(preFilters=" + this.preFilters
                    + ", postFilters=" + this.postFilters
                    + ", postProcessors=" + this.postProcessors
                    + ", responseRenderer=" + this.responseRenderer
                    + ")";

        }

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
