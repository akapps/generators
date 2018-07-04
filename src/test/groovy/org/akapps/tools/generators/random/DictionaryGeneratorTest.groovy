package org.akapps.tools.generators.random

import spock.lang.Specification

class DictionaryGeneratorTest extends Specification {

    def "instantiation should refuse an empty dictionary"() {
        when:
        new DictionaryGenerator<>(Collections.emptySet())

        then:
        thrown IllegalArgumentException
    }

    def "NextValue should return pseudo-randomly distributed words from the dictionary"() {
        given:
        def dictionary = ["one", "two", "three", "four", "five", "six", "seven", "eight"]
        def generator = new DictionaryGenerator(dictionary)
        generator.seed = 2018L

        when:
        def results = (0..8).collect { generator.nextValue() }

        then:
        results == ["five", "one", "four", "seven", "two", "four", "three", "three", "eight"]
    }
}
