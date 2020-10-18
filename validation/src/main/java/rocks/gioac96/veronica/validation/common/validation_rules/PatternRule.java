package rocks.gioac96.veronica.validation.common.validation_rules;

import java.util.regex.Pattern;
import lombok.NonNull;
import rocks.gioac96.veronica.validation.common.CommonValidationFailureReasons;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.validation.ValidationException;
import rocks.gioac96.veronica.validation.ValidationRule;

public class PatternRule
    extends ValidationRuleBuilderWithConstantFailureReason {

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

        return fieldValue -> {

            if (!pattern.matcher(fieldValue).find()) {

                throw new ValidationException(failureReason);

            }

        };

    }

}
