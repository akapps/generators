package org.akapps.tools.generators.random

import spock.lang.Specification

class RatioRandomTest extends Specification {

    def "instantiation should fail if range is too small"() {
        when:
        new RatioRandom(1, 1)

        then:
        thrown IllegalArgumentException
    }

    def "instantiation should fail if ratio is over the upper bound of the range"() {
        when:
        new RatioRandom(120, 100)

        then:
        thrown IllegalArgumentException
    }

    def "test should obey the pseudo-random distribution of generated numbers"() {
        given: "a random sequence of [3, 2, 7, 4, 1, 0, 9, 5, 5, 7, 6]"
        def random = new RatioRandom(3, 10)
        random.seed = 1983L

        when:
        def results = (0..10).collect { random.test() }

        then:
        results == [false, true, false, false, true, true, false, false, false, false, false]
    }

    def "test results should be representative of the configured ratio"() {
        given: 'a configured ratio of 67%'
        def random = new RatioRandom(67, 100)

        when: 'we generate a quite big number of tests'
        def nbTests = 10_000
        def numberOfSuccess = 0
        nbTests.times { if (random.test()) numberOfSuccess++ }

        then: "we should have 67% successes (+/- 1%)"
        def successRatio = numberOfSuccess * 100 / nbTests as int
        66 <= successRatio && successRatio <= 68
    }
}
