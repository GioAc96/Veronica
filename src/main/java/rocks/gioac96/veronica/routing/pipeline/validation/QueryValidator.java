package rocks.gioac96.veronica.routing.pipeline.validation;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.routing.pipeline.stages.FilterPayload;
import rocks.gioac96.veronica.routing.pipeline.stages.PreFilter;

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

    protected static ValidationFailureResponse generateValidationFailureResponse(ValidationException e) {

        return ValidationFailureResponse.builder()
            .validationFailureData(e.getValidationFailureData())
            .build();

    }

    @Override
    public FilterPayload filter(@NonNull Request request) {

        try {

            for (Map.Entry<String, FieldValidator> entry : fieldValidators.entrySet()) {

                String fieldName = entry.getKey();
                FieldValidator fieldValidator = entry.getValue();

                fieldValidator.validateField(fieldName, request.getQueryParam(fieldName));

            }

            return FilterPayload.ok();

        } catch (ValidationException e) {

            return FilterPayload.fail(generateValidationFailureResponse(e));

        }

    }

}
