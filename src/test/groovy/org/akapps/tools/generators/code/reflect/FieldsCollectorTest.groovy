package org.akapps.tools.generators.code.reflect

import org.akapps.tools.generators.code.GroovyBook
import org.akapps.tools.generators.code.JavaSoftware
import org.junit.Test

import java.lang.reflect.Field

class FieldsCollectorTest {

    def collector = new FieldsCollector()

    @Test
    void getFieldsToGenerate_JavaObject() {
        def target = new JavaSoftware()
        def fields = collector.getFieldsToGenerate(target)

        assert fields.size() == 5
        assert fields.collect {it.name} == ["group", "artifact", "releaseDate", "annualLicence", "knownReferences"]
    }

    @Test
    void getFieldsToGenerate_GroovyObject() {
        def target = new GroovyBook()
        def fields = collector.getFieldsToGenerate(target)

        assert fields.size() == 4
        assert fields.collect {it.name} == ["title", "pages", "publication", "authors"]
    }

    @Test
    void getFieldsToGenerate_CustomFilter() {
        FieldFilter filter = { Field field -> field.name.startsWith('a') }
        collector.filters << filter

        def fields = collector.getFieldsToGenerate(new JavaSoftware())

        assert fields.size() == 3
        assert fields.collect {it.name} == ["group", "releaseDate", "knownReferences"]
    }

    @Test
    void getFieldsToGenerate_MultipleFilters() {
        FieldFilter nameFilter = { Field field -> (field.name == 'group') }
        FieldFilter typeFilter = { Field field -> Collection.isAssignableFrom(field.type) }
        collector.filters.addAll(nameFilter, typeFilter)

        def fields = collector.getFieldsToGenerate(new JavaSoftware())

        assert fields.size() == 3
        assert fields.collect {it.name} == ["artifact", "releaseDate", "annualLicence"]
    }
}
