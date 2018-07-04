package org.akapps.tools.generators.random;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A pseudo-random generator where several suppliers can be configured with a given weight.
 * <p>
 *     Each time a new value is requested, a test will be issued to determine the supplier that
 *     will be used to actually supply that value.
 *     The probability to be chosen, for each supplier, is the proportion of the global weight that
 *     this supplier represents.
 * </p>
 * <p>
 *     <em>Example :</em> <br>
 *     We configure 3 suppliers : A with a weight of (25), B (60) and C (15) - total weight is 100.
 *     The probability to be the actual supplier of any value generation would be :
 *     <ul>
 *         <li>A : 25%</li>
 *         <li>B : 60%</li>
 *         <li>C : 15%</li>
 *     </ul>
 * <p>
 *     A {@link Builder} helps instantiate a {@code WeightedGenerator} with the weighted suppliers.
 * </p>
 *
 * @author Antoine Kapps
 */
public class WeightedGenerator<T> implements Generator<T> {

    private final Random random = new Random();

    private final List<Pair<Integer, Supplier<T>>> suppliers;
    private final int upperBound;

    private WeightedGenerator(List<Pair<Integer, Supplier<T>>> suppliers, int upperBound) {
        this.suppliers = Collections.unmodifiableList(
                suppliers.stream().sorted(Comparator.comparing(Pair::getLeft))
                        .collect(Collectors.toList()));
        this.upperBound = upperBound;
    }

    @Override
    public T nextValue() {
        final int index = random.nextInt(upperBound);

        return suppliers.stream()
                .filter(pair -> index < pair.getLeft())
                .findFirst().orElseThrow(indexIsGreaterThanUpperBound(index))
                .getRight().get();
    }

    private Supplier<IllegalStateException> indexIsGreaterThanUpperBound(int index) {
        return () -> new IllegalStateException(String.format("Index (%s) is greater or equal to the upper bound (%s). " +
                "The configured upper bound was %s...",
                index, suppliers.stream().mapToInt(Pair::getLeft).max().orElse(0), upperBound));
    }

    void setSeed(long seed) {
        this.random.setSeed(seed);
    }

    public static final class Builder<U> {
        private int upperBound = 0;
        private final List<Pair<Integer, Supplier<U>>> suppliers = new LinkedList<>();

        public Builder<U> add(int weight, Supplier<U> supplier) {
            upperBound += weight;
            suppliers.add(Pair.of(upperBound, supplier));
            return this;
        }

        public WeightedGenerator<U> build() {
            return new WeightedGenerator<>(suppliers, upperBound);
        }
    }
}
