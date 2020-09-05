package rocks.gioac96.veronica.providers;

public interface BuildsSingleInstance extends BuildsInstances {

    @Override
    default boolean buildsMultipleInstances() {

        return false;

    }

}
