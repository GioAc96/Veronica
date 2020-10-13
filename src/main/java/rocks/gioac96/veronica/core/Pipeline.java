package rocks.gioac96.veronica.core;

import java.util.Objects;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.DeclaresPriority;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.PrioritySet;

/**
 * Request pipeline.
 */
public final class Pipeline implements RequestHandler {

    private final PrioritySet<PreFilter> preFilters;
    private final PrioritySet<PostFilter> postFilters;
    private final PrioritySet<PostProcessor> postProcessors;
    private final ResponseRenderer responseRenderer;
    private final RequestHandler requestHandler;

    protected Pipeline(PipelineBuilder b) {

        this.preFilters = b.preFilters;
        this.postFilters = b.postFilters;
        this.postProcessors = b.postProcessors;
        this.responseRenderer = b.responseRenderer;
        this.requestHandler = b.requestHandler;

    }

    public static PipelineBuilder builder() {

        class PipelineBuilderImpl extends PipelineBuilder implements BuildsMultipleInstances {

        }

        return new PipelineBuilderImpl();
    }

    private Response preRender(Request request) {

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
     * @param request request to handle
     * @return the generated response
     */
    public Response handle(@NonNull Request request) {

        Response response;

        try {

            response = preRender(request);

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

                CommonExecutorServices.defaultPriorityExecutorService().execute(
                    () -> postProcessor.process(request, response),
                    ((PostProcessor.Asynchronous) postProcessor).priority()
                );

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
    public abstract static class PipelineBuilder extends Builder<Pipeline> {

        private final PrioritySet<PreFilter> preFilters = new PrioritySet<>();

        private final PrioritySet<PostFilter> postFilters = new PrioritySet<>();

        private final PrioritySet<PostProcessor> postProcessors = new PrioritySet<>();

        private RequestHandler requestHandler;

        private ResponseRenderer responseRenderer;

        public PipelineBuilder preFilter(@NonNull PreFilter preFilter) {

            this.preFilters.add(preFilter);
            return this;

        }

        public PipelineBuilder preFilter(@NonNull PreFilter preFilter, int priority) {

            this.preFilters.add(preFilter, priority);
            return this;

        }

        public PipelineBuilder preFilter(@NonNull Provider<PreFilter> preFilterProvider) {

            if (preFilterProvider instanceof DeclaresPriority) {

                this.preFilters.add(
                    preFilterProvider.provide(),
                    ((DeclaresPriority) preFilterProvider).priority()
                );

            } else {

                this.preFilters.add(preFilterProvider.provide());

            }
            return this;

        }

        public PipelineBuilder postFilter(@NonNull PostFilter postFilter) {

            this.postFilters.add(postFilter);
            return this;

        }

        public PipelineBuilder postFilter(@NonNull PostFilter postFilter, Integer priority) {

            this.postFilters.add(postFilter, priority);
            return this;

        }

        public PipelineBuilder postFilters(@NonNull Provider<PostFilter> postFilterProvider) {

            if (postFilterProvider instanceof DeclaresPriority) {

                return this.postFilter(
                    postFilterProvider.provide(),
                    ((DeclaresPriority) postFilterProvider).priority()
                );

            } else {

                return this.postFilter(postFilterProvider.provide());

            }

        }

        public PipelineBuilder postProcessor(@NonNull PostProcessor postProcessor) {

            this.postProcessors.add(postProcessor);
            return this;

        }

        public PipelineBuilder postProcessor(@NonNull PostProcessor postProcessor, int priority) {

            this.postProcessors.add(postProcessor, priority);
            return this;

        }

        public PipelineBuilder postProcessor(@NonNull Provider<PostProcessor> postProcessorProvider) {

            if (postProcessorProvider instanceof DeclaresPriority) {

                return postProcessor(
                    postProcessorProvider.provide(),
                    ((DeclaresPriority) postProcessorProvider).priority()
                );

            } else {

                return postProcessor(postProcessorProvider.provide());

            }

        }

        public PipelineBuilder responseRenderer(@NonNull ResponseRenderer responseRenderer) {

            this.responseRenderer = responseRenderer;
            return this;

        }

        public PipelineBuilder responseRenderer(@NonNull Provider<ResponseRenderer> responseRenderer) {

            return responseRenderer(responseRenderer.provide());

        }

        public PipelineBuilder requestHandler(@NonNull RequestHandler requestHandler) {

            this.requestHandler = requestHandler;
            return this;

        }

        public PipelineBuilder requestHandler(@NonNull Provider<RequestHandler> requestHandler) {

            return requestHandler(requestHandler.provide());

        }

        public PipelineBuilder combinePipeline(@NonNull Pipeline pipeline) {

            preFilters.addAll(pipeline.preFilters);
            postFilters.addAll(pipeline.postFilters);
            postProcessors.addAll(pipeline.postProcessors);

            if (pipeline.responseRenderer != null) {

                responseRenderer(pipeline.responseRenderer);

            }

            return this;

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && requestHandler != null
                && preFilters.stream().allMatch(Objects::nonNull)
                && postFilters.stream().allMatch(Objects::nonNull)
                && postProcessors.stream().allMatch(Objects::nonNull);

        }

        @Override
        protected Pipeline instantiate() {

            return new Pipeline(this);

        }

    }

}
