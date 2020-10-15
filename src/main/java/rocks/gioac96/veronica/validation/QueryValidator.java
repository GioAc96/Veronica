package rocks.gioac96.veronica.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonHttpStatus;
import rocks.gioac96.veronica.core.PipelineBreakException;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

/**
 * {@link PreFilter} that validates a {@link Request} query.
 */
public class QueryValidator implements PreFilter {

    @Getter
    private final Map<String, FieldValidator> fieldValidators = new HashMap<>();

    @Override
    public void filter(@NonNull Request request) {

        List<ValidationFailureData> validationFailures = new LinkedList<>();

        fieldValidators.forEach((fieldName, fieldValidator) -> {
            try {

                fieldValidator.validateField(fieldName, request.getQueryParam(fieldName));

            } catch (ValidationException e) {

                validationFailures.add(e.getValidationFailureData());

            }
        });

        if (!validationFailures.isEmpty()) {

            throw new PipelineBreakException(Response.builder()
                .httpStatus(CommonHttpStatus.validationFailure())
                .validationFailures(validationFailures)
                .build());

        }

    }

}
