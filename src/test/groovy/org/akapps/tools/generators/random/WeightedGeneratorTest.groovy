package org.akapps.tools.generators.random

import org.apache.commons.lang3.tuple.Pair
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Supplier

class WeightedGeneratorTest extends Specification {

    def "builder should create a supplier list with the upper bound for each supplier"() {
        when:
        def generator = new WeightedGenerator.Builder()
                .add(4, Mock(Supplier))
                .add(3, Mock(Supplier))
                .add(8, Mock(Supplier))
                .build()

        then:
        generator.@suppliers.collect { it.left } == [4, 7, 15]
        generator.@upperBound == 15
    }

    /**
     * This is the case only if the builder is not used and the (private) constructor is called with
     * inconsistent data.
     */
    def "Misconfigured generator would fail to generate values"() {
        given: 'two supplier weight at a total of 20 points'
        def suppliers = [
                Pair.of(14, Mock(Supplier)), Pair.of(20, Mock(Supplier))
        ]
        and: 'a WeightedGenerator configured with an upper bound higher than the total'
        def generator = new WeightedGenerator(suppliers, 25)
        generator.setSeed(140L)  // first generated index will be 21

        when:
        generator.nextValue()

        then:
        def ex = thrown IllegalStateException
        ex.message == "Index (21) is greater or equal to the upper bound (20). The configured upper bound was 25..."
    }

    def "NextValue should return values depending of the weight of each supplier"() {
        given:
        def generator = new WeightedGenerator.Builder()
                .add(10, { "one" })
                .add(5, { "two" })
                .add(8, { "three" })
                .build()
        generator.seed = 123L

        when:
        def results = (0..5).collect { generator.nextValue() }
        // "random" indexes are [1, 11, 14, 12, 17, 4]

        then:
        results == ["one", "two", "two", "two", "three", "one"]
    }

    def "test results should be representative of the configured ratio"() {
        given: 'a configured ratio of 25% / 60% / 15%'
        def generator = new WeightedGenerator.Builder()
                .add(25, { "one" })
                .add(60, { "two" })
                .add(15, { "three" })
                .build()


        when: 'we generate a quite big number of tests'
        def nbTests = 10_000
        def collector = [ one: new AtomicInteger(), two: new AtomicInteger(), three: new AtomicInteger() ]
        nbTests.times {
            def value = generator.nextValue()
            collector.get(value).incrementAndGet()
        }

        then: "the distribution of the results should match the ratio (+/- 1%)"
        def matchAroundOnePercent = { int tested, int expectedRatio ->
            def ratio = tested * 100 / nbTests as int
            assert (expectedRatio - 1) <= ratio && ratio <= (expectedRatio + 1)
            return true
        }
        matchAroundOnePercent(collector.one.get(), 25)
        matchAroundOnePercent(collector.two.get(), 60)
        matchAroundOnePercent(collector.three.get(), 15)
    }
}
