package org.akapps.tools.generators.code.intern

import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.YearMonth
import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class JodaTimeCodeGeneratorTest {

    def generator = new JodaTimeCodeGenerator()

    @Test
    void asInstantiationCode() {
        assert generator.asInstantiationCode(new YearMonth(2015,4)) == "new YearMonth(2015, 4)"
        assert generator.asInstantiationCode(new LocalDate(1983,5,22)) == "new LocalDate(1983, 5, 22)"
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
        def handledTypes = JodaTimeCodeGenerator.handledTypes
        def expected = [
                YearMonth, LocalDate, LocalDateTime, LocalTime, Duration
        ]

        assert handledTypes.size() == expected.size()

        assert handledTypes.containsAll(expected)
        assert expected.containsAll(handledTypes)
    }
}
