package rocks.gioac96.veronica.providers;

public interface BuildsMultipleInstances extends BuildsInstances {

    @Override
    default boolean buildsMultipleInstances() {

        return true;

    }

}
