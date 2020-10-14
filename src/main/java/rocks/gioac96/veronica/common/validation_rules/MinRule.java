package rocks.gioac96.veronica.common.validation_rules;


import lombok.NonNull;
import rocks.gioac96.veronica.providers.Provider;

public class MinRule extends NumericPredicateRule {

    protected Double minValue;

    public MinRule minValue(@NonNull Double minValue) {

        this.minValue = minValue;
        return this;

    }

    public MinRule minValue(@NonNull Provider<Double> minValueProvider) {

        return minValue(minValueProvider.provide());

    }

    @Override
    protected void configure() {

        super.configure();

        predicate(value -> value >= minValue);

    }

}
