package org.akapps.tools.generators.code.dsl

import groovy.transform.PackageScope
import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.MappingGeneratorResolver
import org.akapps.tools.generators.code.intern.CollectionCodeGenerator
import org.akapps.tools.generators.code.intern.JodaTimeCodeGenerator
import org.akapps.tools.generators.code.intern.SimpleTypesCodeGenerator
import org.akapps.tools.generators.code.reflect.BeanNamingStrategy
import org.akapps.tools.generators.code.reflect.FieldFilter

import java.lang.reflect.Field
/**
 * Configuration for GenerateJavaObject
 *
 * @author Antoine Kapps
 */
final class Configuration {

    boolean writeSettersForNullValues = false
    BeanNamingStrategy namingStrategy

    private final Map<Class, CodeGenerator> generators = [:]
    final List<FieldFilter> fieldFilters = []

    @PackageScope Configuration() {}

    def <T> void register(Class<T> type, CodeGenerator<? super T> codeGenerator) {
        generators.put(type, codeGenerator)
    }

    def addIgnoredFields(String... names) {
        fieldFilters.add({Field field -> names.contains(field.name)} as FieldFilter)
    }

    @PackageScope MappingGeneratorResolver buildResolver() {
        def resolver = new MappingGeneratorResolver()

        def defaultGenerators = [:]
        registerHandledTypes(defaultGenerators, new SimpleTypesCodeGenerator())
        defaultGenerators.put(Collection, new CollectionCodeGenerator(resolver))
        registerHandledTypes(defaultGenerators, new JodaTimeCodeGenerator())

        defaultGenerators.putAll(generators)

        resolver.registerAll(defaultGenerators)
        return resolver
    }

    private static void registerHandledTypes(Map registry, CodeGenerator generator) {
        generator.class.handledTypes.each { registry.put(it, generator) }
    }
}
