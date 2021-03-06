package rocks.gioac96.veronica.validation;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.PipelineBreakException;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;

/**
 * {@link PreFilter} that validates a {@link Request} query.
 */
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryValidator implements PreFilter {

    @Getter
    @Setter
    @NonNull
    @Builder.Default
    private Map<String, FieldValidator> fieldValidators = new HashMap<>();

    protected static Response generateValidationFailureResponse(ValidationException e) {

        return CommonResponses.validationFailure(e.getValidationFailureData());

    }

    @Override
    public void filter(@NonNull Request request) {

        try {

            for (Map.Entry<String, FieldValidator> entry : fieldValidators.entrySet()) {

                String fieldName = entry.getKey();
                FieldValidator fieldValidator = entry.getValue();

                fieldValidator.validateField(fieldName, request.getQueryParam(fieldName));

            }

        } catch (ValidationException e) {

            throw new PipelineBreakException(
                e,
                generateValidationFailureResponse(e)
            );

        }

    }

}
