package org.gioac96.veronica.routing.pipeline.validation;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.gioac96.veronica.http.Request;
import org.gioac96.veronica.routing.pipeline.PipelineBreakException;
import org.gioac96.veronica.routing.pipeline.PreFilter;

/**
 * {@link PreFilter} that validates a {@link Request} query.
 */
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryValidator implements PreFilter {

    @Getter
    @Setter
    @NonNull
    private Map<String, FieldValidator> fieldValidators;

    public QueryValidator() {

        fieldValidators = new HashMap<>();

    }

    @Override
    public void filter(@NonNull Request request) throws PipelineBreakException {

        for (Map.Entry<String, FieldValidator> entry : fieldValidators.entrySet()) {

            String fieldName = entry.getKey();
            FieldValidator fieldValidator = entry.getValue();

            fieldValidator.validateField(fieldName, request.getQueryParam(fieldName));

        }

    }

}
