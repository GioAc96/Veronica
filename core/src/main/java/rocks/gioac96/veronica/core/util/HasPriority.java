package rocks.gioac96.veronica.core.util;

public interface HasPriority extends Comparable<PriorityEntry<?>> {

    int getPriority();

    @Override
    default int compareTo(PriorityEntry<?> o) {

        return Integer.compare(this.getPriority(), o.getPriority());

    }

}
