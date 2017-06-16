package org.akapps.tools.generators.code

/**
 * Abstract the ability to find a {@link CodeGenerator} adapted for the generation of code for a given object.
 *
 * @author Antoine Kapps
 */
interface GeneratorResolver {

    final CodeGenerator NULLSAFE_GENERATOR = {Object o -> "null"}

    /**
     * Finds an instance of CodeGenerator able to handle an object and generate code for it
     *
     * @param type the preferred type, such as the one specified by a field
     * @param object the object for which code must be generated -
     *      if {@code null}, the {@link GeneratorResolver#NULLSAFE_GENERATOR} should be proposed
     *
     * @return an adapted generator instance
     */
    def <T, V extends T> CodeGenerator<T> findGenerator(Class<T> type, V object)

}