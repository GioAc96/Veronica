package rocks.gioac96.veronica.util;

import java.util.AbstractSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Abstract set class that allows for aggregate operations on the set.
 *
 * @param <T> Type of the set
 */
public abstract class SetOps<T> extends AbstractSet<T> implements Set<T> {

    /**
     * Checks whether the provided check {@link Function} returns {@code true} at least once when run against all
     * elements of the set.
     *
     * @param check check function to run against the set
     * @return true iff the check function returns {@code true} at least once when run against all elements in the set
     */
    public boolean any(Function<T, Boolean> check) {

        for (T element : this) {

            if (check.apply(element)) {

                return true;

            }

        }

        return false;

    }

    /**
     * Checks whether the provided check {@link Function} always returns {@code true} when run against all elements of
     * the set.
     *
     * @param check check function to run against the set
     * @return true iff the check function always returns {@code true} when run against all elements in the set
     */
    public boolean every(Function<T, Boolean> check) {

        for (T element : this) {

            if (!check.apply(element)) {

                return false;

            }

        }

        return true;

    }

    /**
     * Checks whether the provided check {@link Function} always returns {@code false} when run against all elements of
     * the set.
     *
     * @param check check function to run against the set
     * @return true iff the check function always returns {@code false} when run against all elements in the set
     */
    public boolean none(Function<T, Boolean> check) {

        return !any(check);

    }

    /**
     * Checks whether the provided check {@link Function} returns {@code false} at least once when run against all
     * elements of the set.
     *
     * @param check check function to run against the set
     * @return true iff the check function returns {@code false} at least once when run against all elements in the set
     */
    @SuppressWarnings("unused")
    public boolean notEvery(Function<T, Boolean> check) {

        return !every(check);

    }

    /**
     * Gets the first element of the set that matches a specified criteria, or a default specified value if none
     * matches.
     *
     * @param check criteria to find first matching element of the set
     * @param def   default value to return when no element of the set meets the specified criteria.
     * @return the first element of the set that matches the specified criteria, or a default specified value if none
     * matches
     */
    public T firstOrDefault(Function<T, Boolean> check, T def) {

        for (T element : this) {

            if (check.apply(element)) {

                return element;

            }

        }

        return def;

    }

    /**
     * Gets the first element of the set that matches a specified criteria.
     *
     * @param check criteria to find first matching element of the set
     * @return the first element of the set that matches the specified criteria, or null if none matches
     */
    @SuppressWarnings("unused")
    public T firstOrNull(Function<T, Boolean> check) {

        return firstOrDefault(check, null);

    }

}
