package rocks.gioac96.veronica.validation;

import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.pipeline.PipelineStage;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.util.HasPriority;
import rocks.gioac96.veronica.core.util.PriorityEntry;
import rocks.gioac96.veronica.core.util.PriorityLinkedHashMapBuilder;

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

    public static class QueryValidatorBuilder<D extends HoldsValidationFailureData> extends ConfigurableProvider<QueryValidator<D>> {

        private final PriorityLinkedHashMapBuilder<String, FieldValidator> fieldValidatorsBuilder
            = new PriorityLinkedHashMapBuilder<>();

        protected LinkedHashMap<String, FieldValidator> generateFieldValidators() {

            return fieldValidatorsBuilder.toLinkedHashMap();

        }

        public QueryValidatorBuilder<D> fieldValidator(
            @NonNull String fieldName,
            @NonNull FieldValidator fieldValidator
        ) {

            return fieldValidator(
                fieldName,
                fieldValidator,
                PriorityEntry.DEFAULT_PRIORITY
            );

        }

        public QueryValidatorBuilder<D> fieldValidator(
            @NonNull String fieldName,
            @NonNull FieldValidator fieldValidator,
            @NonNull Integer priority
        ) {

            fieldValidatorsBuilder.put(fieldName, fieldValidator, priority);

            return this;

        }

        public QueryValidatorBuilder<D> fieldValidator(
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

        public QueryValidatorBuilder<D> fieldValidator(
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

        public QueryValidatorBuilder<D> fieldValidator(
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

            LinkedHashMap<String, FieldValidator> draft = fieldValidatorsBuilder.getDraft();

            return draft != null
                && !draft.isEmpty()
                && draft.entrySet().stream().allMatch(entry -> entry.getValue() != null);

        }

        @Override
        protected QueryValidator<D> instantiate() {

            return new QueryValidator<>(this);

        }

    }

}
