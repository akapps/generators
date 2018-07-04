package org.akapps.tools.generators.random;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Abstraction of a component based on a condition to define how to get the next value.
 *
 * @author Antoine Kapps
 */
public class ConditionalGenerator<T> implements Generator<T> {

    private final BooleanSupplier conditionSupplier;
    private final Supplier<T> whenTrueSupplier;
    private final Supplier<T> whenFalseSupplier;

    public ConditionalGenerator(BooleanSupplier conditionSupplier, Supplier<T> whenTrueSupplier, Supplier<T> whenFalseSupplier) {
        this.conditionSupplier = conditionSupplier;
        this.whenTrueSupplier = whenTrueSupplier;
        this.whenFalseSupplier = whenFalseSupplier;
    }

    @Override
    public T nextValue() {
        return conditionSupplier.getAsBoolean() ? whenTrueSupplier.get() : whenFalseSupplier.get();
    }
}
