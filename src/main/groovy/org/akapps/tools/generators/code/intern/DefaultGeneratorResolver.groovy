package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

/**
 * A GeneratorResolver based on the type of the passed object.
 *
 * @author Antoine Kapps
 */
class DefaultGeneratorResolver implements GeneratorResolver {

    private final CodeGenerator nullSafeGenerator = {Object o -> "null"}
    private final simpleTypesGenerator = new SimpleTypesCodeGenerator()
    private final arraysGenerator = new ArrayCodeGenerator(this)
    private final collectionsGenerator = new CollectionCodeGenerator(this)

    @Override
    <T> CodeGenerator<T> findGenerator(T object) {
        if (object == null)     return nullSafeGenerator

        if (object.class.isArray())
            return arraysGenerator

        if (object instanceof Collection)
            return collectionsGenerator

        return simpleTypesGenerator
    }
}
