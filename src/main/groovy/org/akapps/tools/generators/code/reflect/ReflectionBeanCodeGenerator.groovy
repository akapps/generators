package org.akapps.tools.generators.code.reflect

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

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

    private final FieldsCollector fieldsCollector = new FieldsCollector()
    private final GeneratorResolver generatorResolver

    BeanNamingStrategy helper = new JavaBeanSetterNamingStrategy()

    /** If true, setters for null values will be generated as well */
    boolean explicit = false

    ReflectionBeanCodeGenerator(GeneratorResolver generatorResolver) {
        this.generatorResolver = generatorResolver
    }

    def addFilteredFields(FieldFilter... filters) {
        fieldsCollector.filters.addAll(filters)
    }

    @Override
    String asInstantiationCode(Object bean) {
        def instance = helper.instantiateNew(bean.class)

        def result = [instance.instantiationCode]

        fieldsCollector.getFieldsToGenerate(bean).each { field ->
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
}
