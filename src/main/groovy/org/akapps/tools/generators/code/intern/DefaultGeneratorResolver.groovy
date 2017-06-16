package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

/**
 * A GeneratorResolver based on the type of the passed object.
 *
 * @author Antoine Kapps
 */
class DefaultGeneratorResolver implements GeneratorResolver {

    private final simpleTypesGenerator = new SimpleTypesCodeGenerator()
    private final arraysGenerator = new ArrayCodeGenerator(this)
    private final collectionsGenerator = new CollectionCodeGenerator(this)

    @Override
    <T, V extends T> CodeGenerator<T> findGenerator(Class<T> type, V object) {
        if (object == null)     return NULLSAFE_GENERATOR

        if (type.isArray())
            return arraysGenerator

        if (Collection.isAssignableFrom(type))
            return collectionsGenerator

        return simpleTypesGenerator
    }
}
