package rocks.gioac96.veronica.core;

import java.util.Collection;
import java.util.Objects;
import java.util.PriorityQueue;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonExecutorServices;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;
import rocks.gioac96.veronica.util.PriorityEntry;

/**
 * Request pipeline.
 */
public final class Pipeline implements RequestHandler {

    private final PriorityQueue<PriorityEntry<PreFilter>> preFilters;
    private final PriorityQueue<PriorityEntry<PostFilter>> postFilters;
    private final PriorityQueue<PriorityEntry<PostProcessor>> postProcessors;
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

        preFilters.forEach(preFilterEntry -> preFilterEntry.getValue().filter(request));

        Response response = requestHandler.handle(request);

        postFilters.forEach(postFilterEntry -> postFilterEntry.getValue().filter(request, response));

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

        postProcessors.forEach(postProcessorEntry -> {

            PostProcessor postProcessor = postProcessorEntry.getValue();

            if (postProcessor instanceof PostProcessor.Asynchronous) {

                CommonExecutorServices.defaultPriorityExecutorService().execute(
                    () -> postProcessor.process(request, response),
                    ((PostProcessor.Asynchronous) postProcessor).priority()
                );

            } else {

                postProcessor.process(request, response);

            }

        });

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

        private final PriorityQueue<PriorityEntry<PreFilter>> preFilters = new PriorityQueue<>();
        private final PriorityQueue<PriorityEntry<PostFilter>> postFilters = new PriorityQueue<>();
        private final PriorityQueue<PriorityEntry<PostProcessor>> postProcessors = new PriorityQueue<>();

        private RequestHandler requestHandler;

        private ResponseRenderer responseRenderer;

        public PipelineBuilder preFilter(@NonNull PreFilter preFilter) {

            this.preFilters.add(new PriorityEntry<>(preFilter));
            return this;

        }

        public PipelineBuilder preFilter(@NonNull PreFilter preFilter, int priority) {

            this.preFilters.add(new PriorityEntry<>(preFilter, priority));
            return this;

        }

        public PipelineBuilder preFilter(@NonNull Provider<PreFilter> preFilterProvider) {

            if (preFilterProvider instanceof HasPriority) {

                return this.preFilter(
                    preFilterProvider.provide(),
                    ((HasPriority) preFilterProvider).getPriority()
                );

            } else {

                return this.preFilter(preFilterProvider.provide());

            }

        }

        public PipelineBuilder postFilter(@NonNull PostFilter postFilter) {

            this.postFilters.add(new PriorityEntry<>(postFilter));
            return this;

        }

        public PipelineBuilder postFilter(@NonNull PostFilter postFilter, int priority) {

            this.postFilters.add(new PriorityEntry<>(postFilter, priority));
            return this;

        }

        public PipelineBuilder postFilter(@NonNull Provider<PostFilter> postFilterProvider) {

            if (postFilterProvider instanceof HasPriority) {

                return this.postFilter(
                    postFilterProvider.provide(),
                    ((HasPriority) postFilterProvider).getPriority()
                );

            } else {

                return this.postFilter(postFilterProvider.provide());

            }

        }

        public PipelineBuilder postProcessor(@NonNull PostProcessor postProcessor) {

            this.postProcessors.add(new PriorityEntry<>(postProcessor));
            return this;

        }

        public PipelineBuilder postProcessor(@NonNull PostProcessor postProcessor, int priority) {

            this.postProcessors.add(new PriorityEntry<>(postProcessor, priority));
            return this;

        }

        public PipelineBuilder postProcessor(@NonNull Provider<PostProcessor> postProcessorProvider) {

            if (postProcessorProvider instanceof HasPriority) {

                return this.postProcessor(
                    postProcessorProvider.provide(),
                    ((HasPriority) postProcessorProvider).getPriority()
                );

            } else {

                return this.postProcessor(postProcessorProvider.provide());

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
                && preFilters != null
                && postFilters != null
                && postProcessors != null
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
