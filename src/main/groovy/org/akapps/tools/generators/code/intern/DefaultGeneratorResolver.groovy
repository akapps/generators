package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

/**
 * A basic GeneratorResolver implementation, referencing all Generators, that returns the best-matching one.
 *
 * @author Antoine Kapps
 */
class DefaultGeneratorResolver implements GeneratorResolver {

    private final simpleTypesGenerator = new SimpleTypesCodeGenerator()
    private final arraysGenerator = new ArrayCodeGenerator(this)
    private final collectionsGenerator = new CollectionCodeGenerator(this)
    private final jodaTimeGenerator = {Object o -> JodaTimeWriter.toCode(o)}

    @Override
    <T, V extends T> CodeGenerator<T> findGenerator(Class<T> type, V object) {
        if (object == null)     return NULLSAFE_GENERATOR

        if (type.isArray())
            return arraysGenerator

        if (Collection.isAssignableFrom(type))
            return collectionsGenerator

        if (type.name.startsWith('org.joda.time.'))
            return jodaTimeGenerator

        return simpleTypesGenerator
    }
}
