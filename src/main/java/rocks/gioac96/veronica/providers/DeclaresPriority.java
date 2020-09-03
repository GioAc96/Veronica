package rocks.gioac96.veronica.providers;

/**
 * Interface for providers that declare priorities of the provided objects.
 */
public interface DeclaresPriority {

    /**
     * Gets the priority assigned to the instantiated object. Defaults to null.
     *
     * @return the priority assigned to the instantiated object.
     */
    Integer priority();

}
