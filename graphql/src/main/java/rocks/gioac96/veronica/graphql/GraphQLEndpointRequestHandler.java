package rocks.gioac96.veronica.graphql;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import graphql.GraphQL;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.RequestHandler;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;

@Getter
public class GraphQLEndpointRequestHandler implements RequestHandler {

    private final GraphQL graphQL;
    private final QueryErrorHandler queryErrorHandler;
    private final Gson gson;

    protected GraphQLEndpointRequestHandler(GraphQLEndpointRequestHandlerBuilder b) {

        this.graphQL = b.graphQL;
        this.queryErrorHandler = b.queryErrorHandler;
        this.gson = b.gson;

    }

    public static GraphQLEndpointRequestHandlerBuilder builder() {

        return new GraphQLEndpointRequestHandlerBuilder();

    }

    @Override
    public Response handle(Request request) {

        try {

            Map<String, String> requestBody = gson.fromJson(
                request.getBody(),
                new TypeToken<Map<String, String>>() {
                }.getType()
            );

            String responseBody = gson.toJson(graphQL.execute(requestBody.get("query")));

            return Response.builder()
                .httpStatus(HttpStatus.OK)
                .header("Content-type", "Application/JSON")
                .body(responseBody)
                .provide();

        } catch (Exception e) {

            return queryErrorHandler.handle(e, request);

        }

    }

    public static class GraphQLEndpointRequestHandlerBuilder extends ConfigurableProvider<GraphQLEndpointRequestHandler> {

        private GraphQL graphQL;
        private QueryErrorHandler queryErrorHandler;
        private Gson gson = new Gson();

        @Override
        protected boolean isValid() {

            return graphQL != null
                && gson != null
                && queryErrorHandler != null;

        }

        @Override
        protected GraphQLEndpointRequestHandler instantiate() {

            return new GraphQLEndpointRequestHandler(this);

        }

        public GraphQLEndpointRequestHandlerBuilder graphQL(@NonNull GraphQL graphQL) {

            this.graphQL = graphQL;
            return this;

        }


        public GraphQLEndpointRequestHandlerBuilder graphQL(@NonNull Provider<GraphQL> graphQLProvider) {

            return graphQL(graphQLProvider.provide());

        }

        public GraphQLEndpointRequestHandlerBuilder queryErrorHandler(
            @NonNull QueryErrorHandler queryErrorHandler
        ) {

            this.queryErrorHandler = queryErrorHandler;
            return this;

        }

        public GraphQLEndpointRequestHandlerBuilder queryErrorHandler(
            @NonNull Provider<QueryErrorHandler> queryErrorHandlerProvider
        ) {

            return queryErrorHandler(queryErrorHandlerProvider.provide());

        }

        public GraphQLEndpointRequestHandlerBuilder gson(
            @NonNull Gson gson
        ) {

            this.gson = gson;
            return this;

        }

        public GraphQLEndpointRequestHandlerBuilder gson(
            @NonNull Provider<Gson> gsonProvider
        ) {

            return gson(gsonProvider.provide());

        }

    }

}
