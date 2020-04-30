package rocks.gioac96.veronica.factories;

import static rocks.gioac96.veronica.util.PrioritySet.DEFAULT_PRIORITY;

/**
 * Factory with priority.
 * @param <T> type of the object instantiated by the factory
 */
public interface PriorityFactory<T> extends Factory<T> {

    /**
     * Gets the priority assigned to the instantiated object. Defaults to null.
     *
     * @return the priority assigned to the instantiated object.
     */
    default Integer priority() {

        return DEFAULT_PRIORITY;

    }

}
