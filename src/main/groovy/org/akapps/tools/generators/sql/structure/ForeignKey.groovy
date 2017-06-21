package org.akapps.tools.generators.sql.structure

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * @author Antoine Kapps
 */
@EqualsAndHashCode
@ToString
class ForeignKey extends Constraint {

    @TupleConstructor
    @EqualsAndHashCode
    @ToString
    static class Reference {
        Column from
        String targetColumn
    }

    String targetTable
    List<Reference> participators

    ForeignKey(Map map) {
        this.name = map.name
        this.targetTable = map.targetTable
        this.participators = map.references.collect { fromList(it) }
    }

    private static Reference fromList(List ref) {
        return new Reference(ref[0] as Column, ref[1] as String)
    }
}
