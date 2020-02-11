package org.gioac96.veronica.routing.pipeline;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 * Builder is extensible with lombok's {@link lombok.experimental.SuperBuilder}.
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

    protected Pipeline(PipelineBuilder<?, ?> b) {
        this.preFilters = b.preFilters;
        this.postFilters = b.postFilters;
        this.postProcessors = b.postProcessors;
        this.responseRenderer = b.responseRenderer;
    }

    @Generated
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static PipelineBuilder<?, ?> builder() {

        return new PipelineBuilderImpl();

    }

    private void applyPreFilters(Request request) throws PipelineBreakException {

        for (PreFilter preFilter : preFilters) {

            preFilter.filter(request);

        }

    }

    private void applyPostFilters(Request request, Response response) throws PipelineBreakException {

        for (PostFilter preFilter : postFilters) {

            preFilter.filter(request, response);

        }

    }

    private void applyPostProcessors(Request request, Response response) {

        for (PostProcessor postProcessor : postProcessors) {

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
    public Response handle(@NonNull Request request, @NonNull RequestHandler requestHandler) {

        // Pre-render
        Response response = preRender(request, requestHandler);

        // Rendering
        if (responseRenderer != null) {

            try {

                response.render(responseRenderer);

            } catch (ResponseRenderingException e) {

                response = e.getResponse();

            }

        }

        // Post-render
        applyPostProcessors(request, response);

        return response;

    }

    private Response preRender(Request request, RequestHandler requestHandler) {

        try {

            applyPreFilters(request);

            Response response = requestHandler.handle(request);

            applyPostFilters(request, response);

            return response;

        } catch (PipelineBreakException e) {

            return e.getResponse();

        }

    }

    @Generated
    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class PipelineBuilder<C extends Pipeline, B extends PipelineBuilder<C, B>> {

        private @NonNull PrioritySet<PreFilter> preFilters = new PrioritySet<>();
        private @NonNull PrioritySet<PostFilter> postFilters = new PrioritySet<>();
        private @NonNull PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();
        private ResponseRenderer responseRenderer;

        public PipelineBuilder preFilters(PreFilter... preFilters) {

            Collections.addAll(this.preFilters, preFilters);

            return self();

        }

        public PipelineBuilder preFilters(Collection<PreFilter> preFilters) {

            this.preFilters.addAll(preFilters);

            return self();

        }

        public PipelineBuilder postFilters(PostFilter... postFilters) {

            Collections.addAll(this.postFilters, postFilters);

            return self();

        }

        public PipelineBuilder postFilters(Collection<PostFilter> postFilters) {

            this.postFilters.addAll(postFilters);

            return self();

        }

        public PipelineBuilder postProcessors(PostProcessor... postProcessors) {

            Collections.addAll(this.postProcessors, postProcessors);

            return self();

        }

        public PipelineBuilder postProcessors(Collection<PostProcessor> postProcessors) {

            this.postProcessors.addAll(postProcessors);

            return self();

        }

        public B responseRenderer(ResponseRenderer responseRenderer) {

            this.responseRenderer = responseRenderer;

            return self();

        }

        protected abstract B self();

        public abstract C build();

        public String toString() {

            return "Pipeline.PipelineBuilder(preFilters=" + this.preFilters
                + ", postFilters=" + this.postFilters
                + ", postProcessors=" + this.postProcessors
                + ", responseRenderer=" + this.responseRenderer + ")";

        }
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
