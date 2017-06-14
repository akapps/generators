package org.akapps.tools.generators.code

/**
 * Abstraction of a component able to generate code.
 *
 * @author Antoine Kapps
 */
interface CodeGenerator<T> {

    /**
     * Generate code for instantiating a similar object
     *
     * @param value the object we want to generate code for - not {@code null}
     * @return the code that will instantiate a new object with the same value as {@code value}
     */
    String asInstantiationCode(T value)

}