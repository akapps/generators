package org.akapps.tools.generators.code.intern

import groovy.transform.CompileStatic
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.YearMonth
import org.junit.Test

@CompileStatic
class JodaTimeWriterTest {

    @Test
    void toCode_YearMonth() {
        def tested = new YearMonth(1983, 5)
        assert JodaTimeWriter.toCode(tested) == 'new YearMonth(1983, 5)'
    }

    @Test
    void toCode_LocalDate() {
        def tested = new LocalDate(2015, 6, 12)
        assert JodaTimeWriter.toCode(tested) == 'new LocalDate(2015, 6, 12)'
    }

    @Test
    void toCode_LocalTime_WithMillis() {
        def tested = new LocalTime(12, 0, 0, 243)
        assert JodaTimeWriter.toCode(tested) == 'new LocalTime(12, 0, 0, 243)'
    }

    @Test
    void toCode_LocalTime_NoMillis() {
        def tested = new LocalTime(12, 0, 47, 0)
        assert JodaTimeWriter.toCode(tested) == 'new LocalTime(12, 0, 47)'
    }

    @Test
    void toCode_LocalTime_NoSeconds() {
        def tested = new LocalTime(12, 20, 0, 0)
        assert JodaTimeWriter.toCode(tested) == 'new LocalTime(12, 20)'
    }

    @Test
    void toCode_LocalTime_NoMinutes() {
        def tested = new LocalTime(17, 0, 0, 0)
        assert JodaTimeWriter.toCode(tested) == 'new LocalTime(17, 0)'
    }

    @Test
    void toCode_LocalDateTime() {
        def tested = new LocalDateTime(2017, 6, 17, 8, 28)

        def coded = JodaTimeWriter.toCode(tested)
        assert coded == 'LocalDateTime.parse("2017-06-17T08:28:00.000")'
        assert tested == new GroovyShell().evaluate("org.joda.time.${coded}")
    }

    @Test
    void toCode_Duration_WithMillis() {
        def tested = new Duration(5_447)
        assert JodaTimeWriter.toCode(tested) == 'new Duration(5447)'
    }

    @Test
    void toCode_Duration_NoMillis() {
        def tested = new Duration(45_000)
        assert JodaTimeWriter.toCode(tested) == 'Duration.standardSeconds(45)'
    }

    @Test
    void toCode_Duration_NoSeconds() {
        def tested = new Duration(200 * 60 * 1000)
        assert JodaTimeWriter.toCode(tested) == 'Duration.standardMinutes(200)'
    }

    @Test
    void toCode_Duration_NoMinute() {
        def tested = new Duration(15 * 60 * 60 * 1000)
        assert JodaTimeWriter.toCode(tested) == 'Duration.standardHours(15)'
    }

    @Test
    void toCode_Duration_NoHours() {
        def tested = new Duration(3 * 24 * 3600 * 1000)
        assert JodaTimeWriter.toCode(tested) == 'Duration.standardDays(3)'
    }
}
