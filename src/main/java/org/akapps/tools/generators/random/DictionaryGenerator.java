package org.akapps.tools.generators.random;

import org.apache.commons.lang3.Validate;

import java.util.*;

/**
 * Value generation based on a dictionary.
 * <p>
 *   Each generated value is picked from a collection of values, the index of the chosen item being
 *   a pseudo-random, uniformly distributed int value.
 * </p>
 *
 * @author Antoine Kapps
 */
public class DictionaryGenerator<T> implements Generator<T> {

    private final Random random = new Random();
    private final List<T> dictionary;

    public DictionaryGenerator(Collection<T> dictionary) {
        Validate.notEmpty(dictionary, "Dictionary cannot be an empty collection");
        this.dictionary = Collections.unmodifiableList(new ArrayList<>(dictionary));
    }

    @Override
    public T nextValue() {
        return dictionary.get(random.nextInt(dictionary.size()));
    }

    void setSeed(long seed) {
        this.random.setSeed(seed);
    }
}
