package org.akapps.tools.generators.random;

/**
 * Abstraction of the concept of a generator component.
 *
 * @author Antoine Kapps
 */
public interface Generator<T> {

    /**
     * Generate next object.
     *
     * @return next generated object
     */
    T nextValue();

}
