package org.akapps.tools.generators.code.intern

import groovy.transform.CompileStatic
import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

import java.lang.reflect.Array
/**
 * A generator that handles the instantiation of Arrays.
 * <p>
 *     Code for instantiation of the contained values is obtained by calling a {@link GeneratorResolver}
 *     for each one of them, then delegating the code generation to it.
 * </p>
 *
 * @author Antoine Kapps
 */
@CompileStatic
class ArrayCodeGenerator implements CodeGenerator<Object> {

    private final GeneratorResolver generatorResolver

    ArrayCodeGenerator(GeneratorResolver generatorResolver) {
        this.generatorResolver = generatorResolver
    }

    @Override
    String asInstantiationCode(Object value) {
        if ( ! value.class.isArray())
            throw new IllegalArgumentException("This generator is only applicable for Array types - found ${value.class}")

        def arrayType = value.class.componentType

        if (Array.getLength(value) == 0) {
            return "new ${arrayType.simpleName}[0]"
        } else {
            def arrayValues = value.collect { generatorResolver.findGenerator(it).asInstantiationCode(it) }
            return "new ${arrayType.simpleName}[]{ ${arrayValues.join(', ')} }"
        }
    }
}
