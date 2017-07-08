package org.akapps.tools.generators.code.reflect

import groovy.transform.CompileStatic
import org.akapps.tools.generators.util.ReflectionUtils

import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Collects fields from a Java object, analyzing its structure using Reflection.
 * <p>
 *     Some criteria might be specified to exclude some fields
 *
 * @author Antoine Kapps
 */
@CompileStatic
class FieldsCollector {

    final List<FieldFilter> filters = []

    List<Field> getFieldsToGenerate(Object object) {
        return getAttributes(object.class, addsToFilters(new NonAttributeFilter()))
    }

    List<Field> getFieldsToGenerate(GroovyObject gobject) {
        return getAttributes(gobject.class, addsToFilters(new GroovyFieldsFilter(), new NonAttributeFilter()))
    }

    private List<FieldFilter> addsToFilters(FieldFilter... additionalFilters) {
        return (List) [*additionalFilters, *filters]
    }

    private static List<Field> getAttributes(Class type, List<FieldFilter> list) {
        List<Field> flatten = (List) ReflectionUtils.getClassHierarchy(type).collect { it.getDeclaredFields() }.flatten()
        flatten.removeAll { Field field ->
            list.any { it.exclude field }
        }

        return flatten
    }

    static class NonAttributeFilter implements FieldFilter {
        @Override
        boolean exclude(Field field) {
            def modifiers = field.modifiers
            return Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)
        }
    }

    static class GroovyFieldsFilter implements FieldFilter {

        private static final List GROOVY_FIELDS = ['$staticClassInfo', '$staticClassInfo$', '__$stMC', 'metaClass']

        @Override
        boolean exclude(Field field) {
            return GROOVY_FIELDS.contains(field.name)
        }
    }

}
