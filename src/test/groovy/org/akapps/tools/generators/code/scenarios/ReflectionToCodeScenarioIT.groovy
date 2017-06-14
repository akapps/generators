package org.akapps.tools.generators.code.scenarios

import groovy.transform.CompileStatic
import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.ReflectionBeanCodeGenerator
import org.akapps.tools.generators.code.JavaSoftware
import org.akapps.tools.generators.code.intern.DefaultGeneratorResolver
import org.junit.Test

/**
 * Scenarios for tests about Java code generation for a given Java / Groovy object
 *
 * @author Antoine Kapps
 */
@CompileStatic
class ReflectionToCodeScenarioIT {

    /** Sample class to test groovy objects */
    static class GroovyBook {
        String title
        int pages
        Date publication
        List<String> authors
    }

    CodeGenerator generator = new ReflectionBeanCodeGenerator(new DefaultGeneratorResolver())

    @Test
    void asInstantiationCode_SimpleGroovyObject() {
        def tested = new GroovyBook(title: 'Hello World !',
                pages: 15,
                publication: Date.parse("dd.MM.yyyy", "05.05.2017"),
                authors: ["John Doe", "Jane Wright"])

        def lines = generator.asInstantiationCode(tested)

        def expected = [
                'final GroovyBook groovyBook = new GroovyBook();',
                'groovyBook.setTitle("Hello World !");',
                'groovyBook.setPages(15);',
                'groovyBook.setPublication(new Date(1493935200000L));',
                'groovyBook.setAuthors(Arrays.asList("John Doe", "Jane Wright"));'
        ]
        assert lines == expected.join('\n')
    }

    @Test
    void asInstantiationCode_SimpleJavaObject() {
        def tested = new JavaSoftware("com.company", "licencedProduct", new BigDecimal("49.99"),
                ["www.my-blog.org", "www.twitter.com"] as Set)

        def lines = generator.asInstantiationCode(tested)

        def expected = [
                'final JavaSoftware javaSoftware = new JavaSoftware();',
                'javaSoftware.setGroup("com.company");',
                'javaSoftware.setArtifact("licencedProduct");',
                'javaSoftware.setAnnualLicence(new BigDecimal("49.99"));',
                'javaSoftware.setKnownReferences(new LinkedHashSet<>(Arrays.asList("www.my-blog.org", "www.twitter.com")));'
        ]
        assert lines == expected.join('\n')
    }
}
