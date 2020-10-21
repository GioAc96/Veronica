package rocks.gioac96.veronica.graphql;

import java.util.concurrent.ExecutorService;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

public interface QueryErrorHandler {

    Response handle(Exception e, Request request);

}
