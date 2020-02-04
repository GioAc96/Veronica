package org.gioac96.veronica.routing.matching;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.util.ArraySet;

@AllArgsConstructor
public class PathRule implements RequestMatchingRule {

    @Getter
    @Setter
    @NonNull
    private ArraySet<String> allowedPathPatterns;

    @Override
    public boolean matches(Request request) {

        return allowedPathPatterns.any(
            allowedPathPattern -> allowedPathPattern.matches(request.getPath())
        );

    }

}
