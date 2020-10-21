package rocks.gioac96.veronica.samples.graphql;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.graphql.GraphQLEndpointRequestHandler;

public class GraphQLSample {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(GraphQLEndpointRequestHandler.builder()
                .graphQL(new GraphQLProvider())
                .queryErrorHandler((e, request) -> CommonResponses.badRequest())
                .provide())
            .provide()
            .start();

    }

}
