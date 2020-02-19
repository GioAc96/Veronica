package rocks.gioac96.veronica.routing.pipeline.validation;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.routing.pipeline.PipelineBreakException;
import rocks.gioac96.veronica.routing.pipeline.PreFilter;

/**
 * {@link PreFilter} that validates a {@link Request} query.
 */
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryValidator<Q extends Request> implements PreFilter<Q> {

    @Getter
    @Setter
    @NonNull
    private Map<String, FieldValidator> fieldValidators;

    public QueryValidator() {

        fieldValidators = new HashMap<>();

    }

    @Override
    public void filter(@NonNull Q request) throws PipelineBreakException {

        for (Map.Entry<String, FieldValidator> entry : fieldValidators.entrySet()) {

            String fieldName = entry.getKey();
            FieldValidator fieldValidator = entry.getValue();

            fieldValidator.validateField(fieldName, request.getQueryParam(fieldName));

        }

    }

}
