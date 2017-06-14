package org.akapps.tools.generators.code

import org.akapps.tools.generators.util.ReflectionUtils

import java.lang.reflect.Field

/**
 * @author Antoine Kapps
 */
class ReflectionBeanCodeGenerator implements CodeGenerator<Object> {

    private static final GROOVY_FIELDS = ['$staticClassInfo', '$staticClassInfo$', '__$stMC', 'metaClass']

    private final GeneratorResolver generatorResolver

    /** If true, setters for null values will be generated as well */
    boolean explicit = false

    ReflectionBeanCodeGenerator(GeneratorResolver generatorResolver) {
        this.generatorResolver = generatorResolver
    }

    @Override
    String asInstantiationCode(Object bean) {
        def type = bean.class
        def name = type.simpleName.uncapitalize()

        def result = []

        result << "final ${type.simpleName} $name = new ${type.simpleName}();"

        getFieldsToGenerate(bean).each { field ->
            field.accessible = true
            def value = field.get(bean)

            if (value != null || explicit) {
                def generatedCode = generatorResolver.findGenerator(value).asInstantiationCode(value)
                result << "${name}.set${field.name.capitalize()}($generatedCode);"
            }
        }
        // TODO One single String is a poor signature and must be enhanced
        return result.join('\n')
    }

    private List<Field> getFieldsToGenerate(Object object) {
        return ReflectionUtils.getClassHierarchy(object.class).collect { it.getDeclaredFields() }.flatten()
    }

    private List<Field> getFieldsToGenerate(GroovyObject gobject) {
        return ReflectionUtils.getClassHierarchy(gobject.class).collect { it.getDeclaredFields() }.flatten().findAll {
            ! (it.name in GROOVY_FIELDS)
        }
    }
}
