package rocks.gioac96.veronica.providers;

/**
 * Declares that a {@link Builder} builds multiple instances.
 */
public interface BuildsMultipleInstances extends BuildsInstances {

    @Override
    default boolean buildsMultipleInstances() {

        return true;

    }

}
