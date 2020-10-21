package rocks.gioac96.veronica.graphql;

import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

public interface GraphQLQueryErrorHandler {

    Response handle(Exception e, Request request);

}
