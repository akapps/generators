package org.akapps.tools.generators.code

import groovy.transform.CompileStatic
import org.akapps.tools.generators.code.intern.ArrayCodeGenerator
import org.akapps.tools.generators.util.ReflectionUtils

/**
 * A GeneratorResolver that can be configured to provide an adapted CodeGenerator by types.
 *
 * @author Antoine Kapps
 */
class MappingGeneratorResolver implements GeneratorResolver {

    private final Map<Class, CodeGenerator> mapping = [:]

    private final arraysGenerator = new ArrayCodeGenerator(this)

    void register(Class type, CodeGenerator generator) {
        mapping.put(type, generator)
    }

    void registerAll(Map<Class, CodeGenerator> configMap) {
        mapping.putAll(configMap)
    }

    @Override
    <T, V extends T> CodeGenerator<T> findGenerator(Class<T> type, V object) {
        if (object == null)
            return NULLSAFE_GENERATOR

        // Special case
        if (type.isArray())
            return arraysGenerator

        // Best match for the target type :
        def gen = mapping.get(type)
        if (gen != null)
            return gen

        // Well then, best match for the real type
        gen = mapping.get(object.class)
        if (gen != null)
            return gen

        return closestMatch(type)
    }

    private CodeGenerator closestMatch(Class type) {
        def candidates = mapping.findAll {key, value -> key.isAssignableFrom(type)}

        if (candidates.isEmpty())
            throw new NoSuchElementException("No CodeGenerator found for $type")

        return uniqueValueOr(candidates) {
            def byDistance = candidates.groupBy { ReflectionUtils.distance(type, it.key) }
            def min = byDistance.keySet().min()

            return uniqueValueOr(byDistance.get(min)) { Map self ->
                throw new NoSuchElementException("No CodeGenerator found for $type\nMultiple candidates have been found, for types :"
                        + self.keySet().collect {"\n   - $it"})
            }
        }
    }

    @CompileStatic
    private static <V> V uniqueValueOr(Map<?, V> self, Closure other) {
        if (self.size() == 1)
            return self.values().first()

        return other(self)
    }

}
