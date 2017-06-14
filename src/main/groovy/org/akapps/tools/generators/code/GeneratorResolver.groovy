package org.akapps.tools.generators.code

/**
 * Abstract the ability to find a {@link CodeGenerator} adapted for the generation of code for a given object.
 *
 * @author Antoine Kapps
 */
interface GeneratorResolver {

    def <T> CodeGenerator<T> findGenerator(T object)

}