package org.akapps.tools.generators.code

import org.akapps.tools.generators.util.ReflectionUtils

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * A Generator for generic Java object.
 * <p>
 *     The object's class is analysed using Reflection. Generated code includes a default Constructor call,
 *     then a setter for each non-null attribute of the object.<br/>
 *     At this stage, the generator does not check whether the called constructor / methods really exist.
 * </p>
 *
 * @author Antoine Kapps
 */
class ReflectionBeanCodeGenerator implements CodeGenerator<Object> {

    private static final GROOVY_FIELDS = ['$staticClassInfo', '$staticClassInfo$', '__$stMC', 'metaClass']

    private final GeneratorResolver generatorResolver

    BeanNamingStrategy helper = new JavaBeanSetterNamingStrategy()

    /** If true, setters for null values will be generated as well */
    boolean explicit = false

    ReflectionBeanCodeGenerator(GeneratorResolver generatorResolver) {
        this.generatorResolver = generatorResolver
    }

    @Override
    String asInstantiationCode(Object bean) {
        def instance = helper.instanciateNew(bean.class)

        def result = [instance.instanciationCode]

        getFieldsToGenerate(bean).each { field ->
            field.accessible = true
            def value = field.get(bean)

            if (value != null || explicit) {
                def generatedCode = generatorResolver.findGenerator(field.type, value).asInstantiationCode(value)
                result << helper.setAttributeValue(instance.variableName, field.name, generatedCode)
            }
        }

        // TODO One single String is a poor signature and must be enhanced
        return helper.join(result)
    }

    private List<Field> getFieldsToGenerate(Object object) {
        return getAttributes(object.class, null)
    }

    private List<Field> getFieldsToGenerate(GroovyObject gobject) {
        return getAttributes(gobject.class) { ! (it.name in GROOVY_FIELDS) }
    }

    private List<Field> getAttributes(Class type, Closure filter) {
        return ReflectionUtils.getClassHierarchy(type).collect { it.getDeclaredFields() }.flatten().findAll { Field field ->
            def ok = filter == null || filter.call(field)
            return ok && !Modifier.isStatic(field.modifiers) && !Modifier.isTransient(field.modifiers)
        }
    }
}
