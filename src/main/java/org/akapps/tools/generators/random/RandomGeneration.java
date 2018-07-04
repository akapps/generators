package org.akapps.tools.generators.random;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class regarding random generation. Can be used as a Groovy Category class.
 *
 * @author Antoine Kapps
 */
public class RandomGeneration {

    private RandomGeneration() {
        throw new AssertionError("Not instantiable");
    }

    public static <T> Generator<T> randomly(Collection<T> dictionary) {
        return new DictionaryGenerator<>(dictionary);
    }

    public static <T extends Enum<T>> Generator<T> randomly(Class<T> enumType) {
        return new DictionaryGenerator<>(EnumSet.allOf(enumType));
    }

    public static <T> Generator<T> randomlyWithRate(Collection<T> dictionary, int percent, Predicate<T> condition) {
        final RatioRandom random = new RatioRandom(percent, 100);
        final Map<Boolean, List<T>> partition = dictionary.stream().collect(Collectors.partitioningBy(condition));

        final DictionaryGenerator<T> percentDictionary = new DictionaryGenerator<>(partition.get(Boolean.TRUE));
        final DictionaryGenerator<T> otherDictionary = new DictionaryGenerator<>(partition.get(Boolean.FALSE));
        return new ConditionalGenerator<>(
            random::test,
            percentDictionary::nextValue,
            otherDictionary::nextValue
        );
    }

}
