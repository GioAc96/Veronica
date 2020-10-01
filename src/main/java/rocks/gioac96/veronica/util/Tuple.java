package rocks.gioac96.veronica.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Tuple<T, S> {

    private final T first;

    private final S second;

}
