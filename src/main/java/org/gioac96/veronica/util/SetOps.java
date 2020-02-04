package org.gioac96.veronica.util;

import java.util.AbstractSet;
import java.util.Set;
import java.util.function.Function;

public abstract class SetOps<T> extends AbstractSet<T> implements Set<T> {

    public static <U> ArraySet<U> of(Class<U> type, U... elements) {

        ArraySet<U> result = new ArraySet<>();

        for (U element : elements) {

            result.add(element);

        }

        return result;

    }

    public boolean any(Function<T, Boolean> check) {

        for (T element : this) {

            if (check.apply(element)) {

                return true;

            }

        }

        return false;

    }

    public boolean every(Function<T, Boolean> check) {

        for (T element : this) {

            if (!check.apply(element)) {

                return false;

            }

        }

        return true;

    }

    public boolean none(Function<T, Boolean> check) {

        return !any(check);

    }

    public boolean notEvery(Function<T, Boolean> check) {

        return !every(check);

    }

    public T firstOrDefault(Function<T, Boolean> check, T def) {

        for (T element : this) {

            if (check.apply(element)) {

                return element;

            }

        }

        return def;

    }

}
