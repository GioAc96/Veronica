package rocks.gioac96.veronica.routing.pipeline.stages;

import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

/**
 * PostProcessor implementation that is executed asynchronously.
 * @param <Q> type of the Request
 * @param <S> type of the response
 */
public interface AsynchronousPostProcessor<Q extends Request, S extends Response> extends PostProcessor<Q, S> {

    /**
     * Gets the priority of the thread that executes the PostProcessor.
     * @return the priority of the thread.
     */
    default int getThreadPriority() {

        return Thread.NORM_PRIORITY;

    }

}
