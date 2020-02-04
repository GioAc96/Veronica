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
    private final PrioritySet<PreFilter> preFilters;

    @Getter
    private final PrioritySet<PostFilter> postFilters;

    @Getter
    private final PrioritySet<PostProcessor> postProcessors;

    @Getter
    @Setter
    @NonNull
    private ResponseRenderer responseRenderer;

    public Pipeline(@NonNull ResponseRenderer responseRenderer) {

        preFilters = new PrioritySet<>();
        postFilters = new PrioritySet<>();
        postProcessors = new PrioritySet<>();

        this.responseRenderer = responseRenderer;

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
     * @return the generated resposne
     */
    public Response handle(@NonNull Request request, @NonNull RequestHandler requestHandler) {

        Response response;

        try {

            applyPreFilters(request);

            response = requestHandler.handle(request);

            applyPostFilters(request, response);

            response.render(responseRenderer);

        } catch (PipelineBreakException e) {

            response = e.getResponse();

            if (!response.isRendered()) {

                response.render(responseRenderer);

            }

        }

        applyPostProcessors(request, response);

        return response;

    }

}
