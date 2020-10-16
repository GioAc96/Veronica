package rocks.gioac96.veronica.common.validation_rules;

import java.util.regex.Pattern;
import lombok.NonNull;
import rocks.gioac96.veronica.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationFailureData;
import rocks.gioac96.veronica.validation.ValidationRule;

public class PatternRule
    extends ValidationRuleBuilderWithConstantFailureReason
    implements BuildsMultipleInstances {

    protected Pattern pattern;

    public PatternRule pattern(@NonNull Pattern pattern) {

        this.pattern = pattern;
        return this;

    }

    public PatternRule pattern(@NonNull Provider<Pattern> patternProvider) {

        return pattern(patternProvider.provide());

    }

    public PatternRule patternString(@NonNull String pattern) {

        return pattern(Pattern.compile(pattern));

    }


    public PatternRule patternString(@NonNull Provider<String> patternProvider) {

        return patternString(patternProvider.provide());

    }

    @Override
    protected boolean isValid() {

        return super.isValid()
            && pattern != null;

    }

    @Override
    protected void configure() {

        if (failureReason == null) {

            failureReason = CommonValidationFailureReasons.patternNotMatches();

        }

        super.configure();

    }

    @Override
    protected ValidationRule instantiate() {

        return (fieldName, fieldValue) -> {

            if (! pattern.matcher(fieldValue).find()) {

                throw new ValidationException(ValidationFailureData.builder()
                    .fieldName(fieldName)
                    .failureReason(failureReason)
                    .build());

            }

        };

    }

}
