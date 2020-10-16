package rocks.gioac96.veronica.validation;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonHttpStatus;
import rocks.gioac96.veronica.core.PipelineBreakException;
import rocks.gioac96.veronica.core.PreFilter;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.HasPriority;
import rocks.gioac96.veronica.util.PriorityEntry;
import rocks.gioac96.veronica.util.Tuple;

/**
 * {@link PreFilter} that validates a {@link Request} query.
 */
@Getter
public class QueryValidator implements PreFilter {

    private final LinkedHashMap<String, FieldValidator> fieldValidators;

    public QueryValidator(QueryValidatorBuilder b) {

        this.fieldValidators = b.getFieldValidators();

    }

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

    public static QueryValidatorBuilder builder() {

        class QueryValidatorBuilderImpl extends QueryValidatorBuilder implements BuildsMultipleInstances {

        }

        return new QueryValidatorBuilderImpl();

    }
    
    public abstract static class QueryValidatorBuilder extends Builder<QueryValidator> {

        private final PriorityQueue<PriorityEntry<Tuple<String, FieldValidator>>> orderedFieldValidators = new PriorityQueue<>();
        private final HashSet<String> registeredFields = new HashSet<>();

        protected LinkedHashMap<String, FieldValidator> getFieldValidators() {

            LinkedHashMap<String, FieldValidator> fieldValidators = new LinkedHashMap<>();

            orderedFieldValidators.forEach(entry -> fieldValidators.put(entry.getValue().getFirst(), entry.getValue().getSecond()));

            return fieldValidators;

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull String fieldName,
            @NonNull FieldValidator fieldValidator
        ) {

            return fieldValidator(
                fieldName,
                fieldValidator,
                PriorityEntry.DEFAULT_PRIORITY
            );

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull String fieldName,
            @NonNull FieldValidator fieldValidator,
            @NonNull Integer priority
        ) {

            if (registeredFields.contains(fieldName)) {

                orderedFieldValidators.removeIf(entry -> entry.getValue().getFirst().equals(fieldName));
                registeredFields.remove(fieldName);

            }

            registeredFields.add(fieldName);
            orderedFieldValidators.add(new PriorityEntry<>(new Tuple<>(fieldName, fieldValidator)));

            return this;

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull Provider<String> fieldName,
            @NonNull FieldValidator fieldValidator
        ) {

            if (fieldName instanceof HasPriority) {

                return fieldValidator(
                    fieldName.provide(),
                    fieldValidator,
                    ((HasPriority) fieldName).getPriority()
                );

            } else {

                return fieldValidator(
                    fieldName.provide(),
                    fieldValidator
                );

            }

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull String fieldName,
            @NonNull Provider<FieldValidator> fieldValidatorProvider
        ) {

            return fieldValidator(
              fieldName,
              fieldValidatorProvider.provide()
            );

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull Provider<String> fieldName,
            @NonNull Provider<FieldValidator> fieldValidatorProvider
        ) {

            return fieldValidator(
              fieldName,
              fieldValidatorProvider.provide()
            );

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && ! registeredFields.isEmpty()
                && orderedFieldValidators.stream().noneMatch(Objects::isNull)
                && orderedFieldValidators.stream().allMatch(entry -> {
                    return entry.getValue() != null
                        && entry.getValue().getFirst() != null
                        && entry.getValue().getSecond() != null;
                });

        }

        @Override
        protected QueryValidator instantiate() {

            return new QueryValidator(this);

        }

    }

}
