package rocks.gioac96.veronica.routing.pipeline;

public abstract class PipelineFailureException extends Exception {

    public PipelineFailureException() {
    }

    public PipelineFailureException(String message) {
        super(message);
    }

    public PipelineFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineFailureException(Throwable cause) {
        super(cause);
    }

    public PipelineFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
