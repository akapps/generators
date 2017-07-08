package org.akapps.tools.generators.code.dsl

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.akapps.tools.generators.code.reflect.FieldFilter
import org.akapps.tools.generators.code.reflect.ReflectionBeanCodeGenerator

import static groovy.lang.Closure.DELEGATE_ONLY
/**
 * Entry-point of the DSL-based configuration and call to {@link ReflectionBeanCodeGenerator}.
 *
 * @author Antoine Kapps
 */
class GenerateJavaObject {

    private ReflectionBeanCodeGenerator generator

    private GenerateJavaObject(ReflectionBeanCodeGenerator generator) {
        this.generator = generator
    }

    /**
     * Static factory method allowing the DSL-based configuration for a Java object generator
     */
    static GenerateJavaObject withConfig(@DelegatesTo(strategy = DELEGATE_ONLY, value = Configuration) Closure closure) {
        def config = new Configuration()
        def code = closure.rehydrate(config, this, this)
        code.resolveStrategy = DELEGATE_ONLY
        code.call()

        def generator = new ReflectionBeanCodeGenerator(config.buildResolver())
        generator.explicit = config.writeSettersForNullValues

        if (! config.fieldFilters.isEmpty())
            generator.addFilteredFields(config.fieldFilters as FieldFilter[])

        if (config.namingStrategy != null)
            generator.helper = config.namingStrategy

        return new GenerateJavaObject(generator)
    }

    /**
     * Use default configuration
     */
    static GenerateJavaObject defaultGenerator() {
        def config = new Configuration()
        def generator = new ReflectionBeanCodeGenerator(config.buildResolver())

        return new GenerateJavaObject(generator)
    }

    /**
     * Analysis the given object and handle code for its generation.
     *
     * @param object object to generate the code for
     * @param handler closure that handles the code
     */
    void forObject(Object object, @ClosureParams(value = SimpleType, options = ["java.lang.String"]) Closure handler) {
        handler generator.asInstantiationCode(object)
    }
}
