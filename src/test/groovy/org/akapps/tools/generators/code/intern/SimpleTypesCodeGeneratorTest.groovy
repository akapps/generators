package org.akapps.tools.generators.code.intern

import groovy.transform.CompileStatic
import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

@CompileStatic
class SimpleTypesCodeGeneratorTest {

    SimpleTypesCodeGenerator generator = new SimpleTypesCodeGenerator()

    @Test
    void asInstantiationCode_SimpleTypes() {
        assert generator.asInstantiationCode(BigDecimal.valueOf(10)) == 'BigDecimal.valueOf(10)'
        assert generator.asInstantiationCode("Some string") == '"Some string"'
        assert generator.asInstantiationCode(10) == '10'
    }

    @Test
    void asInstantiationCode_TypeNotHandled() {
        def e = shouldFail {
            generator.asInstantiationCode(new File("target/"))
        }

        assert e.class == MissingMethodException
        assert e.message.startsWith('No signature of method:')
    }

    @Test
    void handledTypes() {
        def handledTypes = SimpleTypesCodeGenerator.handledTypes
        def expected = [
                boolean.class, int.class, long.class, float.class, double.class,
                Boolean, Integer, Long, Float, Double,
                BigDecimal, Date, String, Enum
        ]

        assert handledTypes.size() == expected.size()
        assert handledTypes.containsAll(expected)
        assert expected.containsAll(handledTypes)
    }
}