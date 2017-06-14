package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.TypesCodeWriter

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
}
