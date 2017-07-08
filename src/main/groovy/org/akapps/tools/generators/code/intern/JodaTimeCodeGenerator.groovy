package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator

/**
 * A code generator that handles JodaTime types, based upon {@link JodaTimeWriter}
 *
 * @author Antoine Kapps
 */
class JodaTimeCodeGenerator implements CodeGenerator {

    @Override
    String asInstantiationCode(Object value) {
        // We let Groovy dynamically find the appropriate method
        return JodaTimeWriter.toCode(value)
    }

    static List<Class> getHandledTypes() {
        return JodaTimeWriter.metaClass.methods.findAll {
            it.public && it.static && it.parameterTypes.length == 1
        }.collect {
            it.parameterTypes[0].getTheClass()
        }.asImmutable()
    }
}
