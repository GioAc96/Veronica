package org.gioac96.veronica.routing.pipeline;

import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static PipelineBuilder builder() {

        return new PipelineBuilder();

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

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class PipelineBuilder {

        private PrioritySet<PreFilter> preFilters = new PrioritySet<>();
        private PrioritySet<PostFilter> postFilters = new PrioritySet<>();
        private PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();
        private ResponseRenderer responseRenderer = null;

        public PipelineBuilder preFilters(PreFilter... preFilters) {

            Collections.addAll(this.preFilters, preFilters);

            return this;

        }

        public PipelineBuilder preFilters(Collection<PreFilter> preFilters) {

            this.preFilters.addAll(preFilters);

            return this;

        }

        public PipelineBuilder postFilters(PostFilter... postFilters) {

            Collections.addAll(this.postFilters, postFilters);

            return this;

        }

        public PipelineBuilder postFilters(Collection<PostFilter> postFilters) {

            this.postFilters.addAll(postFilters);

            return this;

        }

        public PipelineBuilder postProcessors(PostProcessor... postProcessors) {

            Collections.addAll(this.postProcessors, postProcessors);

            return this;

        }

        public PipelineBuilder postProcessors(Collection<PostProcessor> postProcessors) {

            this.postProcessors.addAll(postProcessors);

            return this;

        }

        public PipelineBuilder responseRenderer(ResponseRenderer responseRenderer) {

            this.responseRenderer = responseRenderer;

            return this;

        }

        public Pipeline build() {

            return new Pipeline(
                preFilters,
                postFilters,
                postProcessors,
                responseRenderer
            );

        }

    }


}
