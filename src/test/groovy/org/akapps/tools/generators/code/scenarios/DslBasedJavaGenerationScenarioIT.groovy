package org.akapps.tools.generators.code.scenarios

import org.akapps.tools.generators.code.JavaSoftware
import org.akapps.tools.generators.code.dsl.GenerateJavaObject
import org.akapps.tools.generators.code.reflect.BuilderNamingStrategy
import org.joda.time.LocalDate
import org.junit.ComparisonFailure
import org.junit.Test

class DslBasedJavaGenerationScenarioIT {

    static final JavaSoftware EXAMPLE = new JavaSoftware('org.akapps', 'brand-new',
            new LocalDate(2017, 5, 22),
            null, [] as Set)

    @Test
    void basicConfiguration() {

        GenerateJavaObject.defaultGenerator().forObject(EXAMPLE) {
            verify it, """
                final JavaSoftware javaSoftware = new JavaSoftware();
                javaSoftware.setGroup("org.akapps");
                javaSoftware.setArtifact("brand-new");
                javaSoftware.setReleaseDate(new LocalDate(2017, 5, 22));
                javaSoftware.setKnownReferences(new LinkedHashSet<>());
                """
        }
    }

    // TODO test each config option

    @Test
    void like_in_the_script() throws Exception {
        GenerateJavaObject.withConfig {

            writeSettersForNullValues = true
            namingStrategy = new BuilderNamingStrategy()
            addIgnoredFields "knownReferences"

            register org.joda.time.LocalDate, { LocalDate date -> return "/* How to handle that ? */"}

        }.forObject(EXAMPLE) {
            verify it, """
                new JavaSoftwareBuilder()
                    .group("org.akapps")
                    .artifact("brand-new")
                    .releaseDate(/* How to handle that ? */)
                    .annualLicence(null)
                    .build();
                """
        }
    }

    private static void verify(String code, String expected) {
        def expectedLines = expected.split('\n').findAll {!it.trim().isEmpty()}

        int commonLeadingSpaces = expectedLines.collect {countLeadingWhitespaces(it)}.min()
        expectedLines = expectedLines.collect { it.substring(commonLeadingSpaces) }

        def normalized = expectedLines.join('\n')
        if (code != normalized)
            throw new ComparisonFailure("Code is different", normalized, code)
    }

    private static int countLeadingWhitespaces(String value) {
        value.takeWhile {it == ' '}.length()
    }
}
