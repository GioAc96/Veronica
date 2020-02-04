package org.gioac96.veronica.routing.pipeline;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.http.Response;
import org.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
public class Pipeline {

    @Getter
    private final PrioritySet<PreFilter> preFilters = new PrioritySet<>();

    @Getter
    private final PrioritySet<PostFilter> postFilters = new PrioritySet<>();

    @Getter
    private final PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();

    @Getter
    @Setter
    private ResponseRenderer responseRenderer;

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

}
