package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.util.ReflectionUtils

/**
 * A code generator that handles simple Java types, based upon {@link TypesCodeWriter}
 *
 * @author Antoine Kapps
 */
class SimpleTypesCodeGenerator implements CodeGenerator<Object> {

    @Override
    String asInstantiationCode(Object value) {
        return TypesCodeWriter.toCode(value)
    }

    static List<Class> getHandledTypes() {
        return TypesCodeWriter.metaClass.methods.findAll {
            it.public && it.static && it.parameterTypes.length == 1
        }.collect {
            it.parameterTypes[0].getTheClass()
        }.collectMany {
            it.primitive ? [it, ReflectionUtils.getWrapperClassFor(it)] : [it]
        }.asImmutable()
    }
}
