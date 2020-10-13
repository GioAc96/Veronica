package rocks.gioac96.veronica.providers;

/**
 * Declares that a {@link Builder} builds single instances.
 */
public interface BuildsSingleInstance extends BuildsInstances {

    @Override
    default boolean buildsMultipleInstances() {

        return false;

    }

}
