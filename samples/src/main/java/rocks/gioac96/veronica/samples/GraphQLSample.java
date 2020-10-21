package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.graphql.GraphQLEndpointRequestHandler;

public class GraphQLSample {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(new GraphQLEndpointRequestHandler())
            .provide()
            .start();

    }

}
