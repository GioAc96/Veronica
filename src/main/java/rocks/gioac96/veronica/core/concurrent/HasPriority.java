package rocks.gioac96.veronica.core.concurrent;

public interface HasPriority extends Comparable<HasPriority> {

    int getPriority();

    @Override
    default int compareTo(HasPriority o) {

        return Integer.compare(this.getPriority(), o.getPriority());

    }

}
