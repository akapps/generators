package org.akapps.tools.generators.code.intern

import groovy.transform.CompileStatic
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.YearMonth

/**
 * How to write code for JodaTime types
 *
 * @author Antoine Kapps
 */
@CompileStatic
class JodaTimeWriter {

    static String toCode(YearMonth ym) {
        return "new YearMonth(${ym.year}, ${ym.monthOfYear})"
    }

    static String toCode(LocalDate ld) {
        return "new LocalDate(${ld.year}, ${ld.monthOfYear}, ${ld.dayOfMonth})"
    }

    static String toCode(LocalTime lt) {
        if (lt.millisOfSecond != 0)     return "new LocalTime(${lt.hourOfDay}, ${lt.minuteOfHour}, ${lt.secondOfMinute}, ${lt.millisOfSecond})"
        if (lt.secondOfMinute != 0)     return "new LocalTime(${lt.hourOfDay}, ${lt.minuteOfHour}, ${lt.secondOfMinute})"
        return "new LocalTime(${lt.hourOfDay}, ${lt.minuteOfHour})"
    }

    static String toCode(LocalDateTime ldt) {
        // For readability reasons, default instantiation uses LocalDateTime.parse("")
        return "LocalDateTime.parse(\"${ldt.toString()}\")"
    }

    static String toCode(Duration value) {
        def period = value.toPeriod()
        if (period.millis != 0)      return "new Duration(${value.millis})"
        if (period.seconds != 0)     return "Duration.standardSeconds(${value.standardSeconds})"
        if (period.minutes != 0)     return "Duration.standardMinutes(${value.standardMinutes})"
        if (period.hours %24 != 0)   return "Duration.standardHours(${value.standardHours})"
        return "Duration.standardDays(${value.standardDays})"
    }

}
