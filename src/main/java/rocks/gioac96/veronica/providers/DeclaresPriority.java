package rocks.gioac96.veronica.factories;

import static rocks.gioac96.veronica.util.PrioritySet.DEFAULT_PRIORITY;

public interface DeclaresPriority {

    /**
     * Gets the priority assigned to the instantiated object. Defaults to null.
     *
     * @return the priority assigned to the instantiated object.
     */
    default Integer priority() {

        return DEFAULT_PRIORITY;

    }

}
