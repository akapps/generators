package org.akapps.tools.generators.code.scenarios

import groovy.transform.CompileStatic
import org.akapps.tools.generators.code.GroovyBook
import org.akapps.tools.generators.code.JavaSoftware
import org.akapps.tools.generators.code.intern.DefaultGeneratorResolver
import org.akapps.tools.generators.code.reflect.BuilderNamingStrategy
import org.akapps.tools.generators.code.reflect.ReflectionBeanCodeGenerator
import org.joda.time.LocalDate
import org.junit.Test

import java.sql.Timestamp

/**
 * Scenarios for tests about Java code generation for a given Java / Groovy object
 *
 * @author Antoine Kapps
 */
@CompileStatic
class ReflectionToCodeScenarioIT {

    ReflectionBeanCodeGenerator generator = new ReflectionBeanCodeGenerator(new DefaultGeneratorResolver())

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
        def tested = new JavaSoftware("com.company", "licencedProduct", new LocalDate(2015, 4, 2),
                new BigDecimal("49.99"),
                ["www.my-blog.org", "www.twitter.com"] as Set)

        def lines = generator.asInstantiationCode(tested)

        def expected = [
                'final JavaSoftware javaSoftware = new JavaSoftware();',
                'javaSoftware.setGroup("com.company");',
                'javaSoftware.setArtifact("licencedProduct");',
                'javaSoftware.setReleaseDate(new LocalDate(2015, 4, 2));',
                'javaSoftware.setAnnualLicence(new BigDecimal("49.99"));',
                'javaSoftware.setKnownReferences(new LinkedHashSet<>(Arrays.asList("www.my-blog.org", "www.twitter.com")));'
        ]
        assert lines == expected.join('\n')
    }

    /**
     * When generating code for a Java object, it is the declared type of this classe's attributes that define the
     * objects our generated code would instantiate.
     * This is a convenient way to avoid problems with proxied objects...
     */
    @Test
    void asInstantiationCode_DeclaredTypeTakesPrecedenceOverDynamic() {
        def tested = new GroovyBook(title: 'For once, static over dynamic',
                pages: 7,
                publication: Timestamp.valueOf("2009-07-12 22:50:00"),
                authors: ["Big Daddy"])

        def lines = generator.asInstantiationCode(tested)

        def expected = [
                'final GroovyBook groovyBook = new GroovyBook();',
                'groovyBook.setTitle("For once, static over dynamic");',
                'groovyBook.setPages(7);',
                'groovyBook.setPublication(new Date(1247431800000L));',
                'groovyBook.setAuthors(Arrays.asList("Big Daddy"));'
        ]
        assert lines == expected.join('\n')
    }

    @Test
    void asInstanciationCode_BuilderTyped() {
        generator.helper = new BuilderNamingStrategy()

        def tested = new GroovyBook(title: 'Builder is a practical pattern',
                pages: 5,
                publication: Timestamp.valueOf("2009-07-12 22:50:00"),
                authors: ["The GOF company"])

        def lines = generator.asInstantiationCode(tested)

        def expected = [
                'new GroovyBookBuilder()',
                '    .title("Builder is a practical pattern")',
                '    .pages(5)',
                '    .publication(new Date(1247431800000L))',
                '    .authors(Arrays.asList("The GOF company"))',
                '    .build();'
        ]
        assert lines == expected.join('\n')
    }
}
