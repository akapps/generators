package org.akapps.tools.generators.random

import spock.lang.Specification

import java.util.function.BooleanSupplier
import java.util.function.Supplier

class ConditionalGeneratorTest extends Specification {

    def condition = Mock(BooleanSupplier)
    def whenTrue = Mock(Supplier)
    def whenFalse = Mock(Supplier)

    def generator = new ConditionalGenerator(condition, whenTrue, whenFalse)

    def "NextValue should use the whenTrueSupplier whenever the condition is true"() {
        given:
        condition.getAsBoolean() >> true

        when:
        generator.nextValue()
        generator.nextValue()

        then:
        2 * whenTrue.get()
        0 * whenFalse.get()
    }

    def "NextValue should use the whenFalseSupplier whenever the condition is false"() {
        given:
        condition.getAsBoolean() >> false

        when:
        generator.nextValue()
        generator.nextValue()

        then:
        0 * whenTrue.get()
        2 * whenFalse.get()
    }
}
