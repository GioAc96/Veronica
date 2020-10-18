package rocks.gioac96.veronica.validation;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.pipeline.PipelineStage;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.util.HasPriority;
import rocks.gioac96.veronica.core.util.PriorityEntry;
import rocks.gioac96.veronica.core.util.PriorityQueueUtils;
import rocks.gioac96.veronica.core.util.Tuple;

@Getter
public class QueryValidator<D extends HoldsValidationFailureData> implements PipelineStage<D> {

    private final LinkedHashMap<String, FieldValidator> fieldValidators;

    public QueryValidator(QueryValidatorBuilder b) {

        this.fieldValidators = b.generateFieldValidators();

    }

    public static QueryValidatorBuilder builder() {

        return new QueryValidatorBuilder();

    }

    @Override
    public Response filter(@NonNull Request request, Response.ResponseBuilder responseBuilder, D pipelineData) {

        fieldValidators.forEach((fieldName, fieldValidator) -> {
            try {

                fieldValidator.validateField(request.getQueryParam(fieldName));

            } catch (ValidationException e) {

                pipelineData.addValidationFailure(fieldName, e.getValidationFailureReason());

            }

        });

        return null;

    }

    public static class QueryValidatorBuilder extends ConfigurableProvider<QueryValidator> {

        private final PriorityQueue<PriorityEntry<Tuple<String, FieldValidator>>> orderedFieldValidators = new PriorityQueue<>();
        private final HashSet<String> registeredFields = new HashSet<>();

        protected LinkedHashMap<String, FieldValidator> generateFieldValidators() {

            return PriorityQueueUtils.transferEntriesToSortedLinkedHashMap(orderedFieldValidators);
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
            @NonNull Provider<String> fieldNameProvider,
            @NonNull FieldValidator fieldValidator
        ) {

            if (fieldNameProvider instanceof HasPriority) {

                return fieldValidator(
                    fieldNameProvider.provide(),
                    fieldValidator,
                    ((HasPriority) fieldNameProvider).getPriority()
                );

            } else {

                return fieldValidator(
                    fieldNameProvider.provide(),
                    fieldValidator
                );

            }

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull String fieldName,
            @NonNull Provider<FieldValidator> fieldValidatorProvider
        ) {

            if (fieldValidatorProvider instanceof HasPriority) {

                return fieldValidator(
                    fieldName,
                    fieldValidatorProvider.provide(),
                    ((HasPriority) fieldValidatorProvider).getPriority()
                );

            } else {

                return fieldValidator(
                    fieldName,
                    fieldValidatorProvider.provide()
                );

            }

        }

        public QueryValidatorBuilder fieldValidator(
            @NonNull Provider<String> fieldName,
            @NonNull Provider<FieldValidator> fieldValidatorProvider
        ) {

            return fieldValidator(
                fieldName.provide(),
                fieldValidatorProvider
            );

        }

        @Override
        protected boolean isValid() {

            return !registeredFields.isEmpty()
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
