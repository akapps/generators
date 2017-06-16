package org.akapps.tools.generators.code.intern

import groovy.transform.CompileStatic
import org.akapps.tools.generators.code.intern.TypesCodeWriter
import org.junit.Test

import java.nio.file.StandardOpenOption

/**
 * Better be statically compile to ensure the right method is called (because of overloading)
 */
@CompileStatic
class TypesCodeWriterTest {

    @Test
    void toCode_String() {
        def tested = "A basic String"
        assert TypesCodeWriter.toCode(tested) == '"A basic String"'
    }

    @Test
    void toCode_String_EscapeSpecialChars() {
        def tested = """A String with "special" characters
            such as new lines"""

        assert TypesCodeWriter.toCode(tested) == '"A String with \\"special\\" characters\\n            such as new lines"'
    }

    @Test
    void toCode_int() {
        int tested = 57
        assert TypesCodeWriter.toCode(tested) == '57'
    }

    @Test
    void toCode_long() {
        long tested = 1_457_777
        assert TypesCodeWriter.toCode(tested) == '1457777L'
    }

    @Test
    void toCode_float() {
        float tested = 7.78
        assert TypesCodeWriter.toCode(tested) == '7.78f'
    }

    @Test
    void toCode_double() {
        double tested = 7.78
        assert TypesCodeWriter.toCode(tested) == '7.78d'
    }

    @Test
    void toCode_boolean() {
        assert TypesCodeWriter.toCode(true) == 'true'
        assert TypesCodeWriter.toCode(false) == 'false'
    }

    @Test
    void toCode_Date() {
        def tested = Date.parse("dd.MM.yyyy HH:mm:ss", "07.06.2017 11:05:44")
        assert TypesCodeWriter.toCode(tested) == 'new Date(1496826344000L)'
    }

    @Test
    void toCode_BigDecimal_NoDecimal() {
        def tested = BigDecimal.valueOf(87)
        assert TypesCodeWriter.toCode(tested) == 'BigDecimal.valueOf(87)'
    }

    @Test
    void toCode_BigDecimal_NoDecimal_negative() {
        def tested = BigDecimal.valueOf(-75)
        assert TypesCodeWriter.toCode(tested) == 'BigDecimal.valueOf(-75)'
    }

    @Test
    void toCode_BigDecimal_Decimal() {
        def tested = new BigDecimal("-78.04")
        assert TypesCodeWriter.toCode(tested) == 'new BigDecimal("-78.04")'
    }

    @Test
    void toCode_Enum() {
        def tested = StandardOpenOption.DELETE_ON_CLOSE
        assert TypesCodeWriter.toCode(tested) == 'StandardOpenOption.DELETE_ON_CLOSE'
    }

}
