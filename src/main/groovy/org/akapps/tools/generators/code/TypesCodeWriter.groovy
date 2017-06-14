package org.akapps.tools.generators.code

import groovy.json.StringEscapeUtils
import groovy.transform.CompileStatic

/**
 * How to write basic types as Java code.
 *
 * @author Antoine Kapps
 */
@CompileStatic
class TypesCodeWriter {

    static String toCode(String value) {
        return "\"${StringEscapeUtils.escapeJava(value)}\""
    }

    static String toCode(int value) {
        return String.valueOf(value)
    }

    static String toCode(long value) {
        return String.valueOf(value) + 'L'
    }

    static String toCode(float value) {
        return String.valueOf(value) + 'f'
    }

    static String toCode(double value) {
        return String.valueOf(value) + 'd'
    }

    static String toCode(boolean value) {
        return String.valueOf(value)
    }

    static String toCode(BigDecimal value) {
        if (value.scale() == 0)     return "BigDecimal.valueOf($value)"
        else                        return "new BigDecimal(\"${value.toPlainString()}\")"
    }

    static String toCode(Date value) {
        return "new Date(${value.time}L)"
    }

    static String toCode(Enum value) {
        return "${value.class.simpleName}.${value.name()}"
    }

}
